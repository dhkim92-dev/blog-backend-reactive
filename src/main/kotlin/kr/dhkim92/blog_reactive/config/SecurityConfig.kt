package kr.dhkim92.blog_reactive.config

import kr.dhkim92.blog_reactive.auth.application.JwtService
import kr.dhkim92.blog_reactive.auth.application.oauth2.CustomOAuthRequestResolver
import kr.dhkim92.blog_reactive.auth.application.oauth2.handler.OAuth2LoginFailedHandler
import kr.dhkim92.blog_reactive.auth.application.oauth2.handler.OAuth2LoginSuccessHandler
import kr.dhkim92.blog_reactive.auth.application.oauth2.repository.CookieOAuth2AuthorizationRequestRepository
import kr.dhkim92.blog_reactive.common.jwt.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtService: JwtService,
    private val authorizationRequestRepository: CookieOAuth2AuthorizationRequestRepository,
    private val clientRegistrationRepository: ReactiveClientRegistrationRepository,
    private val oAuth2Config: OAuth2Config,
    private val cookieConfig: CookieConfig,
    @Value("\${cors.allowed-origins}")
    private val corsAllowedOrigins: String,
) {

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
    ): SecurityWebFilterChain {
        return http
            .cors { corsSpec ->
                corsSpec.configurationSource { request ->
                    val configuration = CorsConfiguration()

                    // 환경변수에서 가져온 origins을 콤마로 분리하여 처리
                    val origins = corsAllowedOrigins.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    configuration.allowedOrigins = origins
                    configuration.addAllowedHeader("*")
                    configuration.addAllowedMethod("*")
                    configuration.allowCredentials = true
                    configuration.maxAge = 3600L
                    configuration
                }
            }
            .csrf { it.disable() }
            .requestCache { it.disable() }
            .authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers(HttpMethod.GET,
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/swagger-resources/**"
                    ).permitAll()
                    .pathMatchers(POST, "/api/v1/articles", "/api/v1/article-categories").hasRole("ADMIN")
                    .pathMatchers(PUT, "/api/v1/articles/**", "/api/v1/article-categories/**").hasRole("ADMIN")
                    .pathMatchers(DELETE, "/api/v1/articles/**", "/api/v1/article-categories/**").hasRole("ADMIN")
                    .pathMatchers(PUT, "/api/v1/members/**").authenticated()
                    .pathMatchers(DELETE, "/api/v1/members/**").authenticated()
                    .pathMatchers(POST,"/api/v1/articles/*/comments", "/api/v1/comments/**").authenticated()
                    .pathMatchers(PUT, "/api/v1/articles/*/comments", "/api/v1/comments/**").authenticated()
                    .pathMatchers(DELETE, "/api/v1/articles/*/comments", "/api/v1/comments/**", "/api/v1/auth/jwt/revoke").authenticated()
                    .pathMatchers("/api/v1/files/**").hasRole("ADMIN")
                    .anyExchange().permitAll()
            }
            .oauth2Login { customizer ->
                customizer.authorizationRequestRepository(authorizationRequestRepository)
                customizer.authenticationSuccessHandler(
                    OAuth2LoginSuccessHandler(
                        jwtService,
                        cookieConfig,
                        oAuth2Config
                    )
                )
                customizer.authenticationFailureHandler(
                    OAuth2LoginFailedHandler(oAuth2Config)
                )
                customizer.authorizationRequestResolver(CustomOAuthRequestResolver(clientRegistrationRepository))
            }
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}