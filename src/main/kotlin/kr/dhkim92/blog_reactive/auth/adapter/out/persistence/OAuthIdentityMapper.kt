package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.OAuthIdentityEntity
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.OAuthIdentity
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Component

@Component
class OAuthIdentityMapper {

    fun fromR2dbc(identity: OAuthIdentityEntity): OAuthIdentity {
        return OAuthIdentity(
            id = Id.of(OAuthIdentity::class, identity.id!!),
            authAccountId = Id.of(AuthAccount::class, identity.authAccountId),
            provider = identity.provider,
            providerUserId = identity.providerUserId,
        )
    }

    fun toR2dbc(identity: OAuthIdentity): OAuthIdentityEntity {
        return OAuthIdentityEntity(
            id = identity.id?.value,
            authAccountId = identity.authAccountId.value,
            provider = identity.provider,
            providerUserId = identity.providerUserId
        )
    }
}