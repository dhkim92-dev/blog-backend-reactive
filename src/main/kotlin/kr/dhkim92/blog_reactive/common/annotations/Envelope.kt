package kr.dhkim92.blog_reactive.common.annotations

import org.springframework.http.HttpStatus

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Envelope(
    val status: HttpStatus = HttpStatus.OK,
    val message: String = "success",
    val code: String = "G001"
)