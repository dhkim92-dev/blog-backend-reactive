package kr.dhkim92.blog_reactive.common.resolvers

import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.jwt.JwtPrincipal
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class LoginIdResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Login::class.java) &&
            (
                parameter.parameterType == UUID::class.java ||
                parameter.parameterType == String::class.java ||
                parameter.parameterType == Id::class.java
            )
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        return ReactiveSecurityContextHolder.getContext()
            .map { context ->
                val principal = context.authentication.principal
                if (principal is JwtPrincipal) {
                    if ( principal.memberId == null ) {
                        return@map Mono.error<Any>(ForbiddenException())
                    }
                    when (parameter.parameterType) {
                        UUID::class.java -> UUID.fromString(principal.memberId)
                        String::class.java -> principal.memberId
                        Id::class.java -> Id.of(LoginMember::class, UUID.fromString(principal.memberId))
                        else -> throw IllegalStateException("지원하지 않는 파라미터 타입: ${parameter.parameterType}")
                    }
                } else {
                    throw IllegalStateException("인증 정보가 없습니다.")
                }
            }
    }
}