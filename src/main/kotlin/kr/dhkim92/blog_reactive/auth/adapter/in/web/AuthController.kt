package kr.dhkim92.blog_reactive.auth.adapter.`in`.web

import kr.dhkim92.blog_reactive.auth.application.AuthServiceFacade
import kr.dhkim92.blog_reactive.auth.application.port.`in`.web.AuthApi
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.util.CookieUtil
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.auth.dto.LoginRequest
import kr.dhkim92.blog_reactive.interfaces.auth.dto.LoginResponse
import kr.dhkim92.blog_reactive.interfaces.auth.dto.ReissueJwtRequest
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/v1")
@Validated
class AuthController(
    private val authServiceFacade: AuthServiceFacade
): AuthApi {

    companion object {
        const val REFRESH_TOKEN_COOKIE_NAME = "refresh-token"
        const val REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 14 // 14 days
    }

    @PostMapping("/auth/login-by-email-password")
    @Envelope(status = HttpStatus.CREATED, message = "Login successful", code = "AT001")
    override fun loginByEmailPassword(
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
    override fun reissueJwt(
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
    override fun revokeJwt(
        @Login loginId: UUID
    ): Mono<Void> {
        return CookieUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME)
            .flatMap { jwt ->
                authServiceFacade.logout(Id.of(AuthAccount::class, loginId), jwt)
            }
            .flatMap {
                CookieUtil.deleteCookie(REFRESH_TOKEN_COOKIE_NAME)
            }
    }
}