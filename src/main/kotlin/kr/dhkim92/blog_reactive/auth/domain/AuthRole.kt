package kr.dhkim92.blog_reactive.auth.domain

enum class AuthRole(val role: String) {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER"),
    GUEST("ROLE_GUEST"),
}