package kr.dhkim92.blog_reactive.application.board.post.dto

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Category

data class CreatePostCommand(
    val title: String,
    val content: String,
    val categoryId: Id<Category, Long>
) {

}