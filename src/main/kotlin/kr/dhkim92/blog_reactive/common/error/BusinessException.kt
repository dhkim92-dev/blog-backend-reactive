package kr.dhkim92.blog_reactive.common.error

import kr.dhkim92.blog_reactive.auth.domain.exceptions.ErrorCode

open class BusinessException(
    val code: String,
    val status: Int,
    override val message: String,
    val errors: List<String> = emptyList()
) : RuntimeException(message) {

    constructor(ec: ErrorCode): this(ec.code, ec.status, ec.message, emptyList())
}