package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.auth.application.dto.JwtToken
import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class AuthSession(
    id: Id<AuthSession, UUID>? = null,
    val authAccountId: Id<AuthAccount, UUID>,
    token: String,
    issuedAt: Instant,
    expiresAt: Instant,
    deviceName: String?=null,
): BaseDomainEntity<AuthSession, UUID>(id) {

    var token = token
        private set

    var issuedAt = issuedAt
        private set

    var expiresAt = expiresAt
        private set

    var deviceName = deviceName
        private set

    fun rotateToken(jwt: JwtToken) {
        token = jwt.token
        issuedAt = jwt.issuedAt
        expiresAt = jwt.expiresAt
    }

    companion object {
        fun create(
            account: AuthAccount,
            jwt: JwtToken,
            deviceName: String? = null
        ): AuthSession {
            require(jwt.token.isNotBlank()) { "Token value should not be blank" }
            return AuthSession(
                authAccountId = account.identifier,
                token = jwt.token,
                issuedAt = jwt.issuedAt,
                expiresAt = jwt.expiresAt,
                deviceName =  deviceName
            )
        }
    }
}