package kr.dhkim92.blog_reactive.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    @Value("\${jwt.access-token.secret}")
    val accessTokenSecret: String,
    @Value("\${jwt.access-token.expiry}")
    val accessTokenExpiry: Long,
    @Value("\${jwt.refresh-token.secret}")
    val refreshTokenSecret: String,
    @Value("\${jwt.refresh-token.expiry}")
    val refreshTokenExpiry: Long,
    @Value("\${jwt.issuer}")
    val issuer: String,
    @Value("\${jwt.audience}")
    val audience: String
) {
}