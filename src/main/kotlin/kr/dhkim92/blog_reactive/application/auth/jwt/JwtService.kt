package kr.dhkim92.blog_reactive.application.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import kr.dhkim92.blog_reactive.config.JwtConfig
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class JwtService(
    private val jwtConfig: JwtConfig
) {

    private val accessTokenAlgorithm = Algorithm.HMAC256(jwtConfig.accessTokenSecret)

    private val refreshTokenAlgorithm = Algorithm.HMAC256(jwtConfig.refreshTokenSecret)

    private val accessTokenVerifier = JWT.require(accessTokenAlgorithm)
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()

    private val refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()

    fun generateAccessToken(member: Member): String {
        val now = Instant.now()
        val expiry = now.plusMillis(jwtConfig.accessTokenExpiry)

        return JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(member.id!!.value.toString())
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .withClaim("nickname", member.nickname)
            .withClaim("email", member.email)
            .withArrayClaim("roles", listOf(member.role.role).toTypedArray())
            .sign(accessTokenAlgorithm)
    }

    fun generateRefreshToken(member: Member): String {
        val now = Instant.now()
        val expiry = now.plusMillis(jwtConfig.refreshTokenExpiry)

        return JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(member.id!!.value.toString())
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .sign(refreshTokenAlgorithm)
    }

    fun generateRefreshToken(memberId: Id<Member, UUID>): String {
        val now = Instant.now()
        val expiry = now.plusMillis(jwtConfig.refreshTokenExpiry)

        return JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(memberId.value.toString())
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .sign(refreshTokenAlgorithm)
    }

    fun verifyAccessToken(token: String): DecodedJWT {
        return accessTokenVerifier.verify(token)
    }

    fun verifyRefreshToken(token: String): DecodedJWT {
        return refreshTokenVerifier.verify(token)
    }

    fun decodeAccessToken(token: String): DecodedJWT {
        return JWT.decode(token)
    }

    fun decodeRefreshToken(token: String): DecodedJWT {
        return JWT.decode(token)
    }

    fun getRefreshTokenExpiry(): Long = jwtConfig.refreshTokenExpiry
}