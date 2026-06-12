package kr.dhkim92.blog_reactive.common.jwt

import kr.dhkim92.blog_reactive.auth.application.JwtService
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(private val jwtService: JwtService) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = resolveToken(exchange.request)

        if (token != null) {
            try {
                val jwt = jwtService.verifyAccessToken(token)
                val authentication = createAuthentication(jwt)
                return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
            } catch (e: Exception) {
                // Token validation failed, continue without authentication
            }
        }

        return chain.filter(exchange)
    }

    private fun resolveToken(request: ServerHttpRequest): String? {
        val bearerToken = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    private fun createAuthentication(jwt: com.auth0.jwt.interfaces.DecodedJWT): UsernamePasswordAuthenticationToken {
        val userId = jwt.subject
        val rolesArray = jwt.getClaim("roles").asArray(String::class.java)
        val authorities = rolesArray.map { SimpleGrantedAuthority(it) }

        val principal = JwtPrincipal(
            id = userId,
            email = jwt.getClaim("email").asString(),
            nickname = jwt.getClaim("nickname").asString(),
            roles = rolesArray.toList()
        )

        return UsernamePasswordAuthenticationToken(principal, null, authorities)
    }
}
