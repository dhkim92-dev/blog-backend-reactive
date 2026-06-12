package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.util.UUID

class OAuthIdentity(
    id: Id<OAuthIdentity, UUID>? = null,
    val authAccountId: Id<AuthAccount, UUID>,
    val provider: OAuthProvider,
    val providerUserId: String,
): BaseDomainEntity<OAuthIdentity, UUID>(id) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        return this.identifier.equals( (other as OAuthIdentity).identifier)
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}