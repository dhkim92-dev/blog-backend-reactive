package kr.dhkim92.blog_reactive.auth.application

import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.LogoutUseCase
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthAccountPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthSessionPort
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveAuthSessionPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.common.jwt.JwtService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class LogoutService(
//    private val memberRepository: MemberRepository,
    private val loadAuthSessionPort: LoadAuthSessionPort,
    private val saveAuthSessionPort: SaveAuthSessionPort,
    private val loadAuthAccountPort: LoadAuthAccountPort,
    private val jwtService: JwtService
) : LogoutUseCase {

    @Transactional
    override fun execute(
        loginId: Id<AuthAccount, UUID>,
        refreshToken: String
    ): Mono<Void> {
        return Mono.defer {
            val decodedJWT = try {
                jwtService.decodeRefreshToken(refreshToken)
            } catch (e: Exception) {
                return@defer Mono.error<Void>(UnauthorizedException())
            }

            val subjectMemberId = try {
                UUID.fromString(decodedJWT.subject)
            } catch (e: Exception) {
                return@defer Mono.error<Void>(UnauthorizedException())
            }

            if (loginId.value != subjectMemberId) {
                return@defer Mono.error<Void>(UnauthorizedException())
            }

            loadAuthAccountPort.findById(loginId)
                .switchIfEmpty(Mono.error(UnauthorizedException()))
                .flatMapMany { account ->
                    loadAuthSessionPort.findByAuthAccountId(account.identifier)
                }
                .filter { session ->
                    session.token == refreshToken
                }
                .next()
                .flatMap { session ->
                    saveAuthSessionPort.delete(session).then()
                }
                .then()
        }
    }
}