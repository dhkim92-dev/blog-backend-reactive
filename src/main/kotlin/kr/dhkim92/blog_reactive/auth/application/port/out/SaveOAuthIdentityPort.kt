package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.OAuthIdentity
import reactor.core.publisher.Mono

interface SaveOAuthIdentityPort {

    fun save(entity: OAuthIdentity): Mono<OAuthIdentity>
}