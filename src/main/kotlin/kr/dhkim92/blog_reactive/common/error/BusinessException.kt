package kr.dhkim92.blog_reactive.common.error

open class BusinessException(
    val code: String,
    val status: Int,
    override val message: String,
    val errors: List<String> = emptyList()
) : RuntimeException(message) {
}