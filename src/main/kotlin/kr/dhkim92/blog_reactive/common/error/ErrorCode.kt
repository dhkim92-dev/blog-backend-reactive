package kr.dhkim92.blog_reactive.common.error

enum class ErrorCode(
    val code: String,
    val status: Int,
    val message: String
) {

    // COMMON
    UNAUTHORIZED("G001", 401, "인증 실패"),
    FORBIDDEN("G002", 403, "권한 없음"),
    NOT_FOUND("G003", 404, "Resource Not Found"),
    CONFLICT("G004", 409, "Conflict"),
    INTERNAL_SERVER_ERROR("G005", 500, "Internal Server Error"),
    BAD_REQUEST("G006", 400, "잘못된 입력"),
    VALIDATION_FAILED("G007", 400, "Validation Failed"),
    METHOD_NOT_ALLOWED("G008", 405, "Method Not Allowed"),
    UNSUPPORTED_MEDIA_TYPE("G009", 415, "Unsupported Media Type"),

}