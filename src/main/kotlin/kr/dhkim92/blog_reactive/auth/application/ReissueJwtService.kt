package kr.dhkim92.blog_reactive.auth.application

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import kr.dhkim92.blog_reactive.common.jwt.JwtClaims
import kr.dhkim92.blog_reactive.auth.application.dto.LoginResult
import kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase.ReissueJwtUseCase
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthAccountPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthSessionPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadMemberPort
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveAuthSessionPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.auth.domain.exceptions.NotActiveAuthAccountException
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.common.jwt.JwtService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class ReissueJwtService(
    private val loadAuthSessionPort: LoadAuthSessionPort,
    private val saveAuthSessionPort: SaveAuthSessionPort,
    private val loadAuthAccountPort: LoadAuthAccountPort,
    private val loadMemberPort: LoadMemberPort,
    private val jwtService: JwtService
) : ReissueJwtUseCase {

    private data class ReissueContext(
        val authAccount: AuthAccount,
        val authMember: AuthMember,
        val authSession: AuthSession
    )

    @Transactional
    override fun execute(refreshToken: String): Mono<LoginResult> {
        val decoded = runCatching {
            jwtService.decodeRefreshToken(refreshToken)
        }.getOrElse {
            return Mono.error(UnauthorizedException("Invalid refresh token"))
        }

        val authAccountId = runCatching {
            Id.of(AuthAccount::class, UUID.fromString(decoded.subject))
        }.getOrElse {
            return Mono.error(UnauthorizedException("Invalid refresh token subject"))
        }

        return loadContext(authAccountId,refreshToken)
            .flatMap { context -> reissueTokens(context, decoded, refreshToken) }
    }

    private fun verifyRefreshToken(token: String): Mono<Void> {
        return Mono.fromCallable { jwtService.verifyRefreshToken(token) }
            .then()
            .onErrorMap(JWTVerificationException::class.java) {
                UnauthorizedException("Invalid refresh token")
            }
    }

    private fun loadContext(
        authAccountId: Id<AuthAccount, UUID>,
        refreshToken: String
    ): Mono<ReissueContext> {
        val authAccountMono = loadAuthAccountPort.findById(authAccountId)
            .switchIfEmpty(Mono.error(UnauthorizedException("Unauthorized")))
            .flatMap { authAccount ->
                if (!authAccount.isActive()) {
                    return@flatMap Mono.error(NotActiveAuthAccountException())
                }
                Mono.just(authAccount)
            }

        return authAccountMono.flatMap { authAccount ->
            val authMemberMono = loadMemberPort.findByAuthAccountId(authAccountId)
                .switchIfEmpty(Mono.error(UnauthorizedException("Unauthorized")))

            val authSessionMono = loadAuthSessionPort.findByAuthAccountId(authAccount.identifier)
                .filter { authSession -> authSession.token == refreshToken }
                .next()
                .switchIfEmpty(Mono.error(UnauthorizedException("Refresh token not found or invalid")))

            Mono.zip(
                Mono.just(authAccount),
                authMemberMono,
                authSessionMono
            ).map { tuple ->
                ReissueContext(
                    authAccount = tuple.t1,
                    authMember = tuple.t2,
                    authSession = tuple.t3
                )
            }
        }
    }

    private fun reissueTokens(
        context: ReissueContext,
        decoded: DecodedJWT,
        refreshToken: String
    ): Mono<LoginResult> {
        val refreshTokenExpiresAt = decoded.expiresAt?.toInstant()
            ?: context.authSession.expiresAt
        val now = Instant.now()

        if (!refreshTokenExpiresAt.isAfter(now)) {
            return saveAuthSessionPort.delete(context.authSession)
                .then(Mono.error(UnauthorizedException("Refresh token expired")))
        }

        val claims = JwtClaims.of(context.authAccount, context.authMember)
        val accessToken = jwtService.generateAccessToken(claims)
        val threshold = Instant.now().plus(1, ChronoUnit.DAYS)

        return verifyRefreshToken(refreshToken)
            .then(
                if (refreshTokenExpiresAt.isBefore(threshold)) {
                    val newRefreshToken = jwtService.generateRefreshToken(claims)
                    context.authSession.rotateToken(newRefreshToken)
                    saveAuthSessionPort.save(context.authSession)
                        .thenReturn(
                            LoginResult(
                                accessToken = accessToken.token,
                                refreshToken = newRefreshToken.token
                            )
                        )
                } else {
                    Mono.just(
                        LoginResult(
                            accessToken = accessToken.token,
                            refreshToken = refreshToken
                        )
                    )
                }
            )
    }
}
