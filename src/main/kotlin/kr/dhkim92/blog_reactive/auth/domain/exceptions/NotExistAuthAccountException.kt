package kr.dhkim92.blog_reactive.auth.domain.exceptions

import kr.dhkim92.blog_reactive.common.error.BusinessException

class NotExistAuthAccountException(
    message: String? = null
): BusinessException(
    code = ErrorCode.NOT_EXIST_AUTH_ACCOUNT.code,
    message = message ?: ErrorCode.NOT_EXIST_AUTH_ACCOUNT.message,
    status = ErrorCode.NOT_EXIST_AUTH_ACCOUNT.status
) {
}