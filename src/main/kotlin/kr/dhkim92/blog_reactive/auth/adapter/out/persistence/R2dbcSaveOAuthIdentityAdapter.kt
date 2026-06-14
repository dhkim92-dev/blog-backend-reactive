package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.OAuthIdentityRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveOAuthIdentityPort
import kr.dhkim92.blog_reactive.auth.domain.OAuthIdentity
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class R2dbcSaveOAuthIdentityAdapter(
    private val oAuthIdentityRepository: OAuthIdentityRepository,
    private val oAuthIdentityMapper: OAuthIdentityMapper
): SaveOAuthIdentityPort {

    override fun save(identity: OAuthIdentity): Mono<OAuthIdentity> {
        return oAuthIdentityRepository.save(oAuthIdentityMapper.toR2dbc(identity))
            .map(oAuthIdentityMapper::fromR2dbc)
    }
}