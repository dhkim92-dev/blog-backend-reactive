package kr.dhkim92.blog_reactive.common.error

class InternalServerError(
    message: String = "Internal Server Error",
) : BusinessException (
    status = 500,
    message = message,
    code = "INTERNAL_SERVER_ERROR"
) {
}