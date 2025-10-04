package kr.dhkim92.blog_reactive.common.error

class UnauthorizedException(
    code: String = "UNAUTHORIZED",
    message: String = "Unauthorized",
): BusinessException(
    code = code,
    status = 401,
    message = message,
) {
}