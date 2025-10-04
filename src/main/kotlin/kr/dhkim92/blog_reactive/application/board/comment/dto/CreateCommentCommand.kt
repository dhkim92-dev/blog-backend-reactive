package kr.dhkim92.blog_reactive.application.board.comment.dto

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import java.util.UUID

data class CreateCommentCommand(
    val postId: Id<Article, UUID>,
    val content: String
)
