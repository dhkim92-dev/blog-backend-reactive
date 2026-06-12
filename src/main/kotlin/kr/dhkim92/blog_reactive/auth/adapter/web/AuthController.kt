package kr.dhkim92.blog_reactive.auth.adapter.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.auth.application.AuthServiceFacade
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.util.CookieUtil
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.auth.dto.LoginRequest
import kr.dhkim92.blog_reactive.interfaces.auth.dto.LoginResponse
import kr.dhkim92.blog_reactive.interfaces.auth.dto.ReissueJwtRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Auth", description = "Authentication API")
class AuthController(
    private val authServiceFacade: AuthServiceFacade
) {

    companion object {
        const val REFRESH_TOKEN_COOKIE_NAME = "refresh-token"
        const val REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 14 // 14 days
    }

    @PostMapping("/auth/login-by-email-password")
    @Operation(summary = "Email/Password Login", description = "Login using email and password")
    @Envelope(status = HttpStatus.CREATED, message = "Login successful", code = "AT001")
    fun loginByEmailPassword(
        @RequestBody request: LoginRequest,
    ): Mono<LoginResponse> {
        return authServiceFacade.loginByEmailPassword(request.toCommand())
            .flatMap { loginResult ->
                CookieUtil.setCookie(REFRESH_TOKEN_COOKIE_NAME, loginResult.refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS)
                    .then(Mono.just(LoginResponse.Companion.from(loginResult)))
            }
    }

    @PostMapping("/auth/jwt/reissue")
    @Envelope(status = HttpStatus.CREATED, message = "Login successful", code = "AT001")
    fun reissueJwt(
        @RequestBody request: ReissueJwtRequest,
    ): Mono<LoginResponse> {
        return CookieUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME)
            .switchIfEmpty(Mono.error(IllegalArgumentException("Refresh token is required")))
            .flatMap { jwt ->
                authServiceFacade.reissueJwt(jwt)
            }
            .flatMap { loginResult ->
                CookieUtil.setCookie(REFRESH_TOKEN_COOKIE_NAME, loginResult.refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS)
                    .then(Mono.just(LoginResponse.Companion.from(loginResult)))
            }
            .onErrorResume { error ->
                CookieUtil.deleteCookie(REFRESH_TOKEN_COOKIE_NAME)
                    .then(Mono.error(error))
            }
    }

    @DeleteMapping("/auth/jwt/revoke")
    @Envelope(status = HttpStatus.NO_CONTENT, message = "Logout successful", code = "AT002")
    fun revokeJwt(
        @Login loginId: Id<Member, UUID>
    ): Mono<Void> {
        return CookieUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME)
            .flatMap { jwt ->
                authServiceFacade.logout(loginId, jwt)
            }
            .flatMap {
                CookieUtil.deleteCookie(REFRESH_TOKEN_COOKIE_NAME)
            }
    }
}