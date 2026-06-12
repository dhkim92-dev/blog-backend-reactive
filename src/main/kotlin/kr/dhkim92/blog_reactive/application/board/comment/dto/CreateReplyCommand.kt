package kr.dhkim92.blog_reactive.application.board.comment.dto

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import java.util.UUID

data class CreateReplyCommand(
    val parentId: Id<Comment, UUID>,
    val content: String
) {

}