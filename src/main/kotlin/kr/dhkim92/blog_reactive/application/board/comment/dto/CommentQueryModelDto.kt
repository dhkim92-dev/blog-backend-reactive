package kr.dhkim92.blog_reactive.application.board.comment.dto

import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.board.CommentQueryModel
import kr.dhkim92.blog_reactive.domain.board.r2dbc.CommentQueryRecord
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

data class CommentQueryModelDto(
    val id: Id<Comment, UUID>,
    val postId: Id<Article, UUID>,
    val parentId: Id<Comment, UUID>?,
    val writer: Writer,
    val content: String,
    val replyCount: Int,
    val createdAt: LocalDateTime
) {

    companion object {

        fun from(model: CommentQueryModel): CommentQueryModelDto {
            return CommentQueryModelDto(
                id = model.id,
                postId = model.articleId,
                parentId = model.parentId,
                writer = model.writer,
                content = model.content,
                replyCount = model.replyCount,
                createdAt = model.createdAt,
            )
        }
    }
}