package kr.dhkim92.blog_reactive.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import java.net.URI

@Configuration
class SwaggerConfig {

    @Bean
    fun redirectRoot(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(RequestPredicates.GET("/")) { _ ->
                ServerResponse.temporaryRedirect(URI("/swagger-ui.html")).build()
            }
    }

    @Bean
    fun redirectSwaggerUI(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(RequestPredicates.GET("/swagger-ui")) { _ ->
                ServerResponse.temporaryRedirect(URI("/swagger-ui.html")).build()
            }
    }
}
