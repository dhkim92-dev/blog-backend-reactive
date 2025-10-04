package kr.dhkim92.blog_reactive.domain.member

import kr.dhkim92.blog_reactive.domain.BaseDomainEntity
import kr.dhkim92.blog_reactive.domain.Id
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class RefreshToken(
    val id: Id<RefreshToken, UUID>? = null,
    val memberId: Id<Member, UUID> = Id.of(Member::class, UUID.randomUUID()),
    val token: String = UUID.randomUUID().toString(),
    val expireAt: Instant = Instant.now().plusSeconds(60 * 60 * 24 * 14), // 7 days
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    isDeleted: Boolean = false
) : BaseDomainEntity(createdAt, updatedAt, isDeleted) {

    fun isExpired(): Boolean {
        return Instant.now().isAfter(expireAt)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RefreshToken) return false
        if (id != other.id) return false
        if (token != other.token) return false
        return true
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}