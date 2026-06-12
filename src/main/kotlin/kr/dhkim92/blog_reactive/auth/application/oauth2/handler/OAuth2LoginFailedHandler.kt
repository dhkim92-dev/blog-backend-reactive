package kr.dhkim92.blog_reactive.auth.application.oauth2.handler

import kr.dhkim92.blog_reactive.common.util.CookieUtil
import kr.dhkim92.blog_reactive.config.OAuth2Config
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import reactor.core.publisher.Mono
import java.net.URI

class OAuth2LoginFailedHandler(
    private val oAuth2Config: OAuth2Config
): ServerAuthenticationFailureHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange,
        exception: AuthenticationException?
    ): Mono<Void> {
        val redirectUri = oAuth2Config.redirectTo + "/oauth2-login-failed"
        val response = webFilterExchange.exchange.response
        response.headers.location = URI.create(redirectUri)
        response.statusCode = HttpStatus.FOUND
        return CookieUtil.deleteCookie(oAuth2Config.authorizationRequestCookieName)
            .then()
    }
}