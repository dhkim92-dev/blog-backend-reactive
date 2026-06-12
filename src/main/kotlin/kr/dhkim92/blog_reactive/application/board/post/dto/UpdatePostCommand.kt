package kr.dhkim92.blog_reactive.application.board.post.dto

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import java.util.UUID

data class UpdatePostCommand(
    val postId: Id<Article, UUID>,
    val categoryId: Id<Category, Long>,
    val title: String,
    val content: String,
) {

}