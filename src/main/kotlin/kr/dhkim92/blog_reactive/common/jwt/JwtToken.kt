package kr.dhkim92.blog_reactive.common.jwt

import java.time.Instant

class JwtToken(
    val token: String,
    val issuedAt: Instant,
    val expiresAt: Instant
)