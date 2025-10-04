package kr.dhkim92.blog_reactive.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import reactor.core.publisher.Mono

@Configuration
@EnableR2dbcAuditing
class R2dbcAuditingConfig {

    @Bean
    fun reactiveAuditorAware(): ReactiveAuditorAware<String> {
        return ReactiveAuditorAware {
            ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .switchIfEmpty(Mono.just("system"))
        }
    }
}
