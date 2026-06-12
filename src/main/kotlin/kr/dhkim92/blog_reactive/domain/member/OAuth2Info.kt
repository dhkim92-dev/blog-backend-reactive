package kr.dhkim92.blog_reactive.domain.member

import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.time.LocalDateTime
import java.util.UUID

class OAuth2Info(
    val id: Id<OAuth2Info, UUID>? = null,
    val memberId: Id<Member, UUID> = Id.of(Member::class, UUID.randomUUID()),
    val provider: OAuth2Provider = OAuth2Provider.GOOGLE,
    val userId: String = "",
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    isDeleted: Boolean = false
): BaseDomainEntity(createdAt, updatedAt, isDeleted) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OAuth2Info) return false
        if (id != other.id) return false
        if (provider != other.provider) return false
        if (userId != other.userId) return false
        return true
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}