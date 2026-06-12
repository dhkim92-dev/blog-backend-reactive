package kr.dhkim92.blog_reactive.auth.domain.exceptions

import kr.dhkim92.blog_reactive.common.error.BusinessException
import org.springframework.http.HttpStatus

class InvalidCredentialException(message: String? = null): BusinessException(
    code = ErrorCode.INVALID_CREDENTIAL.code,
    message= message  ?: ErrorCode.INVALID_CREDENTIAL.message,
    status= HttpStatus.BAD_REQUEST.value()
) {
}