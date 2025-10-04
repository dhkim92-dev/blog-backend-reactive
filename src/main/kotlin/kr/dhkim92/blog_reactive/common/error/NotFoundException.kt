package kr.dhkim92.blog_reactive.common.error

class NotFoundException(
    code : String = "NOT_FOUND",
    message: String = "Not Found"
): BusinessException(
    code = code,
    status = 404,
    message = message,
) {

}