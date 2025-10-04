package kr.dhkim92.blog_reactive.common.util

import org.springframework.http.ResponseCookie
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

object CookieUtil {

    fun getCookie(key: String): Mono<String> {
        return Mono.deferContextual { ctx ->
            val exchange = ctx.get(ServerWebExchange::class.java)
            val cookie = exchange.request.cookies[key]?.firstOrNull()
            if (cookie != null) {
                Mono.just(cookie.value)
            } else {
                Mono.empty<String>()
            }
        }
    }

    fun setCookie(
        key: String,
        value: String,
        maxAge: Int,
        path: String = "/",
        httpOnly: Boolean = true,
        secure: Boolean = true,
        sameSite: String = "Lax"
    ): Mono<Void> {
        return Mono.deferContextual { ctx ->
            val exchange = ctx.get(ServerWebExchange::class.java)
            val response = exchange.response
            val cookie = ResponseCookie.from(key, value)
                .httpOnly(httpOnly)
                .secure(secure)
                .path(path)
                .maxAge(maxAge.toLong())
                .sameSite(sameSite)
                .build()
            response.addCookie(cookie)
            Mono.empty<Void>()
        }
    }

    fun deleteCookie(
        key: String,
        path: String = "/",
        httpOnly: Boolean = true,
        secure: Boolean = true,
        sameSite: String = "Lax"
    ): Mono<Void> {
        return Mono.deferContextual { ctx ->
            val exchange = ctx.get(ServerWebExchange::class.java)
            val response = exchange.response
            val cookie = ResponseCookie.from(key, "")
                .httpOnly(httpOnly)
                .secure(secure)
                .path(path)
                .maxAge(0)
                .sameSite(sameSite)
                .build()
            response.addCookie(cookie)
            Mono.empty<Void>()
        }
    }
}