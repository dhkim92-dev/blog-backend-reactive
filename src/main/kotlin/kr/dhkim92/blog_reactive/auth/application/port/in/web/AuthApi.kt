package kr.dhkim92.blog_reactive.auth.application.port.`in`.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.auth.dto.LoginRequest
import kr.dhkim92.blog_reactive.interfaces.auth.dto.LoginResponse
import kr.dhkim92.blog_reactive.interfaces.auth.dto.ReissueJwtRequest
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name="Auth API", description = "Authentication API")
interface AuthApi {

    @Operation(summary = "Email/Password Login", description = "Login using email and password")
    fun loginByEmailPassword(request: LoginRequest, ): Mono<LoginResponse>

    @Operation(summary = "Reissue JWT", description = "Reissue JWT token")
    fun reissueJwt(request: ReissueJwtRequest): Mono<LoginResponse>

    @Operation(summary = "Revoke JWT Refresh token", description = "Revoke JWT refresh token")
    fun revokeJwt(loginId: Id<AuthAccount, UUID>): Mono<Void>
}