package kr.dhkim92.blog_reactive.auth.domain.exceptions

enum class ErrorCode(val code: String, val status: Int, val message: String) {

    INVALID_PASSWORD("A-001", 400, "Invalid password"),
    INVALID_EMAIL("A-002", 400, "Invalid email"),
    INVALID_CREDENTIAL("A-003", 400, "Invalid credentails"),
    NOT_EXIST_OAUTH_IDENTITY(code = "A-004", status = 404, message = "Not exist OAuth Identity"),
    NOT_EXIST_AUTH_ACCOUNT(code = "A-005", status = 404, message = "Not exist Auth Account"),
    NOT_ACTIVE_ACCOUNT(code = "A-006", status = 403, message = "Not active account"),
    ALREADY_EXIST_ACCOUNT(code = "A-007", status = 409, message = "Already exist account")
}