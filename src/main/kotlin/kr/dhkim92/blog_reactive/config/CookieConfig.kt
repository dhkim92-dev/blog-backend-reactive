package kr.dhkim92.blog_reactive.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class CookieConfig(
    @Value("\${cookie.http-only:true}")
    val httpOnly: Boolean,
    @Value("\${cookie.secure:true}")
    val secure: Boolean,
    @Value("\${cookie.same-site:Lax}")
    val sameSite: String
) {
}