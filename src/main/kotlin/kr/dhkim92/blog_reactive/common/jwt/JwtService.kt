package kr.dhkim92.blog_reactive.common.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import kr.dhkim92.blog_reactive.config.JwtConfig
import org.springframework.stereotype.Service
import java.time.Instant

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

    fun generateAccessToken(claims: JwtClaims): JwtToken {
        val now = Instant.now()
        val expiresAt = now.plusMillis(jwtConfig.accessTokenExpiry)
        val token = JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(claims.sub)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .withClaim("memberId", claims.memberId)
            .withClaim("nickname", claims.nickname)
            .withArrayClaim("roles", listOf(claims.role.role).toTypedArray())
            .sign(accessTokenAlgorithm)
        return JwtToken(token = token, issuedAt = now, expiresAt = expiresAt)
    }

    fun generateRefreshToken(claims: JwtClaims): JwtToken {
        val now = Instant.now()
        val expiresAt = now.plusMillis(jwtConfig.refreshTokenExpiry)
        val token = JWT.create()
            .withIssuer(jwtConfig.issuer)
            .withAudience(jwtConfig.audience)
            .withSubject(claims.sub)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .sign(refreshTokenAlgorithm)
        return JwtToken(token = token, issuedAt = now, expiresAt = expiresAt)
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