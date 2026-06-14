package kr.dhkim92.blog_reactive.common.error

import org.springframework.http.HttpStatus

open class ConflictException(
    code: String = "CONFLICT",
    message: String,
): BusinessException(
    status = HttpStatus.CONFLICT.value(),
    message = message,
    code = code
) {
}