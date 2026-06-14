package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.OAuthIdentity
import kr.dhkim92.blog_reactive.auth.domain.OAuthProvider
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Mono
import java.util.UUID

interface LoadOAuthIdentityPort {

    fun findByProviderAndUserId(provider: OAuthProvider, userId: String): Mono<OAuthIdentity>

    fun findByAuthAccountId(authAccountId: Id<AuthAccount, UUID>): Mono<OAuthIdentity>
}