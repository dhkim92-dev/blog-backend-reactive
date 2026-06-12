package kr.dhkim92.blog_reactive.auth.domain.exceptions

import kr.dhkim92.blog_reactive.common.error.BusinessException

class NotExistOAuthIdentityException(
    message: String? = null
): BusinessException(
    code = ErrorCode.NOT_EXIST_OAUTH_IDENTITY.code,
    status = ErrorCode.NOT_EXIST_OAUTH_IDENTITY.status,
    message = message ?: ErrorCode.NOT_EXIST_OAUTH_IDENTITY.message
) {
}