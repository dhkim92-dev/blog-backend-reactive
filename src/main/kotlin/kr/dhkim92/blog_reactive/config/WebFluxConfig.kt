package kr.dhkim92.blog_reactive.config

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
    private val loginIdResolver: LoginIdResolver
) : WebFluxConfigurer {

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(loginIdResolver)
    }
//
//
//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        registry.addResourceHandler("/swagger-ui.html**")
//            .addResourceLocations("classpath:/META-INF/resources/")
//
//        registry.addResourceHandler("/swagger-ui/**")
//            .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
//
//        registry.addResourceHandler("/webjars/**")
//            .addResourceLocations("classpath:/META-INF/resources/webjars/")
//    }

    @Bean
    fun envelopResponseHandler(
        serverCodecConfigurer: ServerCodecConfigurer,
        contentTypeResolver: RequestedContentTypeResolver
    ): EnvelopResponseHandler {
        return EnvelopResponseHandler(serverCodecConfigurer.writers, contentTypeResolver)
    }
}
