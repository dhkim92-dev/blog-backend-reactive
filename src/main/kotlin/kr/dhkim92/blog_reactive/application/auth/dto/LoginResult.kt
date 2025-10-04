package kr.dhkim92.blog_reactive.application.auth.dto

data class LoginResult(
    val type: String = "Bearer",
    val refreshToken: String="",
    val accessToken: String="",
) {
}