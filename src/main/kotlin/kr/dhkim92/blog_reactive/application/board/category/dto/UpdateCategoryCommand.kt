package kr.dhkim92.blog_reactive.application.board.category.dto

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Category

data class UpdateCategoryCommand(
    val id: Id<Category, Long>,
    val name: String
) {
}