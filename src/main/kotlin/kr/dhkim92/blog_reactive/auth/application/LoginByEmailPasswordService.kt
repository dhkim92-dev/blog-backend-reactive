package kr.dhkim92.blog_reactive.auth.application

import kr.dhkim92.blog_reactive.auth.application.dto.LoginCommand
import kr.dhkim92.blog_reactive.auth.application.dto.LoginResult
import kr.dhkim92.blog_reactive.auth.application.JwtService
import kr.dhkim92.blog_reactive.auth.application.dto.JwtClaims
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.AddAuthSessionUseCase
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.CleanExpiredAuthSessionUseCase
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.LoginByEmailPasswordUseCase
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthAccountPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthSessionPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadEmailPasswordCredentialPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadMemberPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import kr.dhkim92.blog_reactive.auth.domain.exceptions.InvalidCredentialException
import kr.dhkim92.blog_reactive.common.data.Email
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
@Service
class LoginByEmailPasswordService(
    private val jwtService: JwtService,
    private val loadMemberPort: LoadMemberPort,
    private val loadAuthAccountPort: LoadAuthAccountPort,
    private val loadEmailPasswordCredentialPort: LoadEmailPasswordCredentialPort,
    private val cleanUpAuthSessionUseCase: CleanExpiredAuthSessionUseCase,
    private val addAuthSessionUseCase: AddAuthSessionUseCase,
    private val passwordEncoder: PasswordEncoder
): LoginByEmailPasswordUseCase {

    private class LoginContext (
        val authAccount: AuthAccount,
    )

    @Transactional
    override fun execute(command: LoginCommand): Mono<LoginResult> {
        return loadEmailPasswordCredentialPort.findByEmail(command.email)
            .switchIfEmpty{ Mono.error(InvalidCredentialException()) }
            .flatMap { credential ->
                if ( !passwordEncoder.matches(command.password.value, credential.password)  ) {
                    return@flatMap Mono.error(InvalidCredentialException())
                }
                loadAuthAccountPort.findById(credential.authAccountId)
                    .switchIfEmpty{ Mono.error { InvalidCredentialException() } }
            }
            .flatMap { account ->
                if ( account.isActive() ) {
                    return@flatMap Mono.error(UnauthorizedException(message = "활성화 되지 않은 계정입니다"))
                }

                val memberMono = loadMemberPort.findById(account.memberId)
                    .switchIfEmpty{ Mono.error { InvalidCredentialException() } }
                val cleanUpMono = cleanUpAuthSessionUseCase.execute(account.identifier)
                    .thenReturn(account)
                Mono.zip(cleanUpMono, memberMono)
            }
            .flatMap { tuple ->
                val account = tuple.t1
                val member = tuple.t2
                val claims = JwtClaims.of( account, member )
                val accessToken = jwtService.generateAccessToken(claims)
                val refreshToken = jwtService.generateRefreshToken(claims)
                val authSession = AuthSession.create(account, refreshToken)
                val addRefreshTokenMono = addAuthSessionUseCase.execute(account.identifier, authSession)
                    .switchIfEmpty { Mono.error(InvalidCredentialException()) }
                    .thenReturn(refreshToken)

                Mono.zip(addRefreshTokenMono, Mono.just(accessToken))
            }
            .flatMap { (refreshToken, accessToken) ->
                Mono.just(LoginResult.of(refreshToken, accessToken))
            }
    }
}