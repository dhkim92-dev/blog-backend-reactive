package kr.dhkim92.blog_reactive.interfaces.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.auth.application.dto.LoginResult

@Schema(description = "Login Response")
class LoginResponse(
    @Schema(description = "Token Type", example = "Bearer")
    val type: String,
    @Schema(description = "Access Token")
    val accessToken: String,
    @Schema(description = "Refresh Token")
    val refreshToken: String
) {

    companion object {
        fun from(result: LoginResult): LoginResponse {
            return LoginResponse(
                type = "Bearer",
                accessToken = result.accessToken,
                refreshToken = result.refreshToken
            )
        }
    }
}