package kr.dhkim92.blog_reactive.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        return OpenAPI()
            .info(apiInfo())
            .addServersItem(Server().url("/"))
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }

    @Bean
    fun memberApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("회원 API")
            .pathsToMatch("/api/v1/members/**")
            .build()
    }

    private fun apiInfo() = Info()
        .title("Blog Reactive API")
        .description("Spring WebFlux로 구현된 블로그 API 문서")
        .version("v1.0.0")
        .license(License().name("Apache 2.0").url("http://springdoc.org"))
}
