package kr.dhkim92.blog_reactive.application.board.category.dto

import kr.dhkim92.blog_reactive.domain.board.Category

data class CategoryDto(
    val id: Long,
    val name: String,
    val count: Int
) {
    companion object {

        fun from(category: Category) = CategoryDto(
            id = category.id?.value ?: 0L,
            name = category.name,
            count = category.count
        )
    }
}