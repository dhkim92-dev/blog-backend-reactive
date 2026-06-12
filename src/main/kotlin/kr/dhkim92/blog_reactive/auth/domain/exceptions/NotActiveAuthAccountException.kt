package kr.dhkim92.blog_reactive.auth.domain.exceptions

import kr.dhkim92.blog_reactive.common.error.BusinessException

class NotActiveAuthAccountException(
    message: String? = null
): BusinessException(
    code = ErrorCode.NOT_ACTIVE_ACCOUNT.code,
    message = message ?: ErrorCode.NOT_ACTIVE_ACCOUNT.message,
    status = ErrorCode.NOT_ACTIVE_ACCOUNT.status
) {
}