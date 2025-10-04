package kr.dhkim92.blog_reactive.common.error

class BadRequestException(
    code: String = "BAD_REQUEST",
    message: String = "Bad Request",
    errors: List<String> = listOf()
) : BusinessException(
    code = code,
    status = 400,
    message = message,
    errors = errors
) {
}