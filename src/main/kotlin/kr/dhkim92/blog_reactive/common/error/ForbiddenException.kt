package kr.dhkim92.blog_reactive.common.error

class ForbiddenException(
    code: String = "FORBIDDEN",
    message: String = "Forbidden",
): BusinessException(
    code = code,
    status = 403,
    message = message,
) {
}