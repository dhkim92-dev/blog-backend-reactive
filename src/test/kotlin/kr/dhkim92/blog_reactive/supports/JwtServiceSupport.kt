package kr.dhkim92.blog_reactive.supports

import io.kotest.core.spec.style.AnnotationSpec
import kr.dhkim92.blog_reactive.common.jwt.JwtService
import kr.dhkim92.blog_reactive.config.JwtConfig
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import org.springframework.beans.factory.annotation.Value
import java.util.UUID

class JwtServiceSupport : AnnotationSpec() {

    fun createJwtConfig(): JwtConfig {
        return JwtConfig(
            accessTokenSecret = "test-access-key",
            accessTokenExpiry = 1000L * 60L * 30L, // 30 minutes
            refreshTokenSecret = "test-refresh-key",
            refreshTokenExpiry = 1000L * 60L * 60L * 24L * 365L * 20L, // 20 years
            issuer = "https://identifier.dohoon-kim.kr",
            audience = "https://www.dohoon-kim.kr"
        )
    }

val member = Member(
        id = Id.of(Member::class, UUID.fromString("01994869-c3d0-787e-8a3b-184296276042")),
        nickname = "admin",
        email = "admin@dohoon-kim.kr",
        password = "hashed-password",
        isBlocked = false,
        isDeleted = false
    )

    @Test
    fun generatePermanentRefreshToken() {
        val jwtService = JwtService(createJwtConfig())
        val token = jwtService.generateRefreshToken(member)
        println(token)
    }
}