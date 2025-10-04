package kr.dhkim92.blog_reactive.common.error

import jakarta.validation.ValidationException
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.client.HttpClientErrorException

class ErrorResponse(
    val code: String,
    val message: String,
    val status: Int,
    val errors: List<FieldError> = emptyList()
) {

    companion object {

        fun from(e: BusinessException): ErrorResponse {
            return ErrorResponse(
                code = e.code,
                message = e.message,
                status = e.status,
            )
        }

        fun from(e: IllegalArgumentException): ErrorResponse {
            return ErrorResponse(
                code = "BAD_REQUEST",
                message = e.message ?: "Invalid argument",
                status = 400,
            )
        }

        fun from(e: IllegalAccessException): ErrorResponse {
            return ErrorResponse(
                code = "FORBIDDEN",
                message = e.message ?: "Access is denied",
                status = 403,
            )
        }

        fun from(e: IllegalStateException): ErrorResponse {
            return ErrorResponse(
                code = "CONFLICT",
                message = e.message ?: "Conflict occurred",
                status = 409,
            )
        }

        fun from(e: Exception): ErrorResponse {
            return ErrorResponse(
                code = "INTERNAL_SERVER_ERROR",
                message = e.message ?: "An unexpected error occurred",
                status = 500,
            )
        }

        fun from(e: HttpClientErrorException.MethodNotAllowed): ErrorResponse {
            return ErrorResponse(
                code = "METHOD_NOT_ALLOWED",
                message = e.message ?: "HTTP method not allowed",
                status = 405,
            )
        }

        fun from(e: ValidationException): ErrorResponse {
            return ErrorResponse(
                code = "VALIDATION_ERROR",
                message = e.message ?: "Validation failed",
                status = 400,
            )
        }

        fun from(e: WebExchangeBindException): ErrorResponse {
            val fieldErrors = FieldError.from(e)
            return ErrorResponse(
                code = "VALIDATION_ERROR",
                message = "Validation failed for one or more fields",
                status = 400,
                errors = fieldErrors
            )
        }
    }

    class FieldError(
        val field: String,
        val value: Any?,
        val reason: String
    ) {

        companion object {
            fun from(errors: WebExchangeBindException): List<FieldError> {
                return errors.bindingResult.fieldErrors.map {
                    FieldError(
                        field = it.field,
                        value = it.rejectedValue,
                        reason = it.defaultMessage ?: "Invalid value"
                    )
                }
            }
        }
    }
}