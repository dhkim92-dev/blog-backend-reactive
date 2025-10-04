package kr.dhkim92.blog_reactive.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class OAuth2Config(
    @Value("\${auth.oauth2.redirect-to}")
    val redirectTo: String,
    @Value("\${auth.oauth2.authorization-request-cookie-name}")
    val authorizationRequestCookieName: String,
    @Value("\${auth.oauth2.authorization-request-cookie-expiry:180}")
    val authCookieExpireSeconds: Int
) {
}