package kr.dhkim92.blog_reactive.common.error

import io.swagger.v3.oas.annotations.Hidden
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Hidden
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> {
        logger.error("BusinessException: ${ex.message}", ex)
        return ResponseEntity
            .status(ex.status)
            .body(ErrorResponse.from(ex))
    }
}

