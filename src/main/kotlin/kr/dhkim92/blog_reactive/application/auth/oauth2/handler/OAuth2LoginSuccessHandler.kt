package kr.dhkim92.blog_reactive.application.auth.oauth2.handler

import kr.dhkim92.blog_reactive.application.auth.jwt.JwtService
import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.CustomOAuth2User
import kr.dhkim92.blog_reactive.config.CookieConfig
import kr.dhkim92.blog_reactive.config.OAuth2Config
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono
import java.net.URI

class OAuth2LoginSuccessHandler(
    private val jwtService: JwtService,
    private val cookieConfig: CookieConfig,
    private val oauth2Config: OAuth2Config
) : ServerAuthenticationSuccessHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        const val REFRESH_TOKEN_KEY = "refresh-token"
    }

    init {
        logger.info("OAuth2LoginSuccessHandler initialized")
        logger.info("Cookie Config cookie secure : $cookieConfig : secure=${cookieConfig.secure}, httpOnly=${cookieConfig.httpOnly}, sameSite=${cookieConfig.sameSite}")
        logger.info("OAuth2 Config : ${oauth2Config.authorizationRequestCookieName}")
    }

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication?
    ): Mono<Void> {
        val userInfo = authentication?.principal as CustomOAuth2User
        val response = webFilterExchange.exchange.response
        val redirectUri = oauth2Config.redirectTo
        response.statusCode = HttpStatus.FOUND
        response.headers.location = URI.create(redirectUri)
        logger.debug("OAuth2 Login Success! Redirect to : $redirectUri")

        return Mono.zip(
            setAuthorizationCookie(webFilterExchange, userInfo.refreshToken),
            deleteAuthorizationCookie(webFilterExchange)
        ).then()
    }

    private fun setAuthorizationCookie(webFilterExchange: WebFilterExchange, value: String): Mono<Void> {
        val response = webFilterExchange.exchange.response
        val cookie = ResponseCookie.from(REFRESH_TOKEN_KEY, value)
            .httpOnly(cookieConfig.httpOnly)
            .secure(cookieConfig.secure)
            .path("/")
            .maxAge((jwtService.getRefreshTokenExpiry() / 1000L))
            .sameSite(cookieConfig.sameSite)
            .build()
        response.addCookie(cookie)
        return Mono.empty()
    }

    private fun deleteAuthorizationCookie(webFilterExchange: WebFilterExchange): Mono<Void> {
        val response = webFilterExchange.exchange.response
        val cookie = ResponseCookie.from(oauth2Config.authorizationRequestCookieName, "")
            .httpOnly(cookieConfig.httpOnly)
            .secure(cookieConfig.secure)
            .path("/")
            .maxAge(0)
            .sameSite(cookieConfig.sameSite)
            .build()
        response.addCookie(cookie)
        return Mono.empty()
    }
}