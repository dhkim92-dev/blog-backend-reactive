package kr.dhkim92.blog_reactive.config

import kr.dhkim92.blog_reactive.common.resolvers.AuthIdResolver
import kr.dhkim92.blog_reactive.common.resolvers.LoginIdResolver
import kr.dhkim92.blog_reactive.common.response.EnvelopResponseHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Configuration
class WebFluxConfig(
    private val loginIdResolver: LoginIdResolver,
    private val authIdResolver: AuthIdResolver
) : WebFluxConfigurer {

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(loginIdResolver)
        configurer.addCustomResolver(authIdResolver)
    }

    @Bean
    fun envelopResponseHandler(
        serverCodecConfigurer: ServerCodecConfigurer,
        contentTypeResolver: RequestedContentTypeResolver
    ): EnvelopResponseHandler {
        return EnvelopResponseHandler(serverCodecConfigurer.writers, contentTypeResolver)
    }
}
