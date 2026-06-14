package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import kr.dhkim92.blog_reactive.auth.domain.OAuthProvider
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface OAuthIdentityRepository : R2dbcRepository<OAuthIdentityEntity, UUID> {

    fun findByProviderAndProviderUserId(provider: OAuthProvider, providerUserId: String): Mono<OAuthIdentityEntity>

    fun findByAuthAccountId(authAccountId: UUID): Mono<OAuthIdentityEntity>
}