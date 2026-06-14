package kr.dhkim92.blog_reactive.common.jwt

/**
 * JWT 인증 정보를 담는 Principal 객체
 */
data class JwtPrincipal(
    val authId: String,
    val memberId: String?,
    val nickname: String?,
    val roles: List<String>
)
