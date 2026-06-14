package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.OAuthIdentityRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadOAuthIdentityPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.OAuthIdentity
import kr.dhkim92.blog_reactive.auth.domain.OAuthProvider
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class R2dbcLoadOAuthIdentityAdapter(
    private val oAuthIdentityRepository: OAuthIdentityRepository,
    private val oAuthIdentityMapper: OAuthIdentityMapper
): LoadOAuthIdentityPort {

    override fun findByProviderAndUserId(provider: OAuthProvider, userId: String): Mono<OAuthIdentity> {
        return oAuthIdentityRepository.findByProviderAndProviderUserId(
            provider, userId
        ).map(oAuthIdentityMapper::fromR2dbc)
    }

    override fun findByAuthAccountId(authAccountId: Id<AuthAccount, UUID>): Mono<OAuthIdentity> {
        return oAuthIdentityRepository.findByAuthAccountId(authAccountId = authAccountId.value)
            .map(oAuthIdentityMapper::fromR2dbc)
    }
}