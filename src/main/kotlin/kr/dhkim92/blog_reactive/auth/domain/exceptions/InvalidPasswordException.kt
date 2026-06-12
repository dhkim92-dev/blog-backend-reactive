package kr.dhkim92.blog_reactive.auth.domain.exceptions

import kr.dhkim92.blog_reactive.common.error.BusinessException

class InvalidPasswordException(
    code: String = ErrorCode.INVALID_PASSWORD.code,
    status: Int = ErrorCode.INVALID_PASSWORD.status,
    message: String = ErrorCode.INVALID_PASSWORD.message
): BusinessException(code, status, message) {
}