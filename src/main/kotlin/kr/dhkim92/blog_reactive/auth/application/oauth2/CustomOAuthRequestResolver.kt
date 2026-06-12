package kr.dhkim92.blog_reactive.auth.application.oauth2

import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class CustomOAuthRequestResolver(
    private val clientRegistrationRepository: ReactiveClientRegistrationRepository
): ServerOAuth2AuthorizationRequestResolver {

    private val defaultResolver =
        DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository)


    override fun resolve(exchange: ServerWebExchange?): Mono<OAuth2AuthorizationRequest?>? {
        return defaultResolver.resolve(exchange).map { customize(it) }
    }

    override fun resolve(
        exchange: ServerWebExchange ,
        clientRegistrationId: String
    ): Mono<OAuth2AuthorizationRequest> {
        return defaultResolver.resolve(exchange, clientRegistrationId).map { customize(it) }
    }

    private fun customize(req: OAuth2AuthorizationRequest): OAuth2AuthorizationRequest {
        return OAuth2AuthorizationRequest.from(req)
            .additionalParameters { params ->
                params["prompt"] = "consent"
            }
            .build()
    }
}