package kr.dhkim92.blog_reactive.common.response

import org.springframework.http.HttpStatus

data class EnvelopResponse<T>(
    val status: HttpStatus = HttpStatus.OK,
    val data: T?,
    val message: String = "success",
    val code: String = "G001"
) {

}

