package kr.dhkim92.blog_reactive.auth.application.dto

data class LoginResult(
    val type: String = "Bearer",
    val refreshToken: String="",
    val accessToken: String="",
) {

    companion object {
        fun of(refreshToken: JwtToken, accessToken: JwtToken): LoginResult {
            return LoginResult(
                type = "Bearer",
                refreshToken = refreshToken.token,
                accessToken = accessToken.token
            )
        }
    }
}