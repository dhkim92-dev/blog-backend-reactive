package kr.dhkim92.blog_reactive.auth.application

import kr.dhkim92.blog_reactive.auth.application.dto.LoginResult
import kr.dhkim92.blog_reactive.auth.application.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.common.jwt.JwtClaims
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.AddAuthSessionUseCase
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.CleanExpiredAuthSessionUseCase
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.LoginByOAuth2UseCase
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthAccountPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadMemberPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadOAuthIdentityPort
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveAuthAccountPort
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.auth.domain.OAuthProvider
import kr.dhkim92.blog_reactive.auth.domain.exceptions.NotActiveAuthAccountException
import kr.dhkim92.blog_reactive.auth.domain.exceptions.NotExistAuthAccountException
import kr.dhkim92.blog_reactive.auth.domain.exceptions.NotExistOAuthIdentityException
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.jwt.JwtService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
class LoginByOAuth2Service(
    private val loadAuthAccountPort: LoadAuthAccountPort,
    private val saveAuthAccountPort: SaveAuthAccountPort,
    private val loadOAuthIdentityPort: LoadOAuthIdentityPort,
    private val loadMemberPort: LoadMemberPort,
    private val cleanExpiredAuthSessionUseCase: CleanExpiredAuthSessionUseCase,
    private val addAuthSessionUseCase: AddAuthSessionUseCase,
    private val jwtService: JwtService
): LoginByOAuth2UseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun execute(userInfo: OAuth2UserInfo): Mono<LoginResult> {
        val provider = OAuthProvider.from(userInfo.getProvider())
        val userId = userInfo.getUserId()

        return loadOAuthIdentityPort.findByProviderAndUserId(provider, userId)
            .switchIfEmpty { Mono.error(NotExistOAuthIdentityException() ) }
            .flatMap { identity ->
                loadAuthAccountPort.findById(identity.authAccountId)
                    .switchIfEmpty { Mono.error(NotExistAuthAccountException()) }
            }
            .flatMap { account ->
                if ( !account.isActive() || account.isDeleted ) {
                    return@flatMap Mono.error(NotActiveAuthAccountException() )
                }

                val cleanUpSessionMono = cleanExpiredAuthSessionUseCase.execute(account.identifier)
                    .thenReturn(account)
                val memberMono = loadMemberPort.findByAuthAccountId(account.identifier)
                    .switchIfEmpty(Mono.error(NotFoundException(message= "Member not exists")))

                Mono.zip(cleanUpSessionMono, memberMono)
            }
            .flatMap { (account, member) ->
                val claims = JwtClaims.of(account, member)
                val accessToken = jwtService.generateAccessToken(claims)
                val refreshToken = jwtService.generateRefreshToken(claims)
                val session = AuthSession.create(account, refreshToken)
                val addSessionMono = addAuthSessionUseCase.execute(session.authAccountId, session)
                    .thenReturn(refreshToken)
                Mono.zip(Mono.just(accessToken), addSessionMono)
            }
            .flatMap { (accessToken, refreshToken) ->
                Mono.just(LoginResult.of(refreshToken, accessToken))
            }

    }
}