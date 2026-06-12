package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.OAuthIdentity
import kr.dhkim92.blog_reactive.auth.domain.OAuthProvider
import reactor.core.publisher.Mono

interface LoadOAuthIdentityPort {

    fun findByProviderAndUserId(provider: OAuthProvider, userId: String): Mono<OAuthIdentity>
}