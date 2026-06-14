package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.jwt.JwtToken
import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.time.Instant
import java.util.UUID

/**
 * @brief AuthSession entity
 * @param id AuthSession id
 * @param authAccountId AuthAccount id
 * @param token JWT token
 * @param issuedAt issued at
 * @param expiresAt expires at
 * @param deviceName device name
 * @param createdAt created at
 * @param updatedAt updated at
 * @param isDeleted deleted flag
 */
class AuthSession(
    id: Id<AuthSession, UUID>? = null,
    val authAccountId: Id<AuthAccount, UUID>,
    token: String,
    issuedAt: Instant,
    expiresAt: Instant,
    deviceName: String?=null,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
    isDeleted: Boolean = false
): BaseDomainEntity<AuthSession, UUID>(id, createdAt, updatedAt, isDeleted) {

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        val that = other as AuthSession
        return id == that.id
    }

    override fun hashCode(): Int {
        if ( id == null ) return 0
        return id.hashCode()
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