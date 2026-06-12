package kr.dhkim92.blog_reactive.application.board.comment.dto

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

data class CommentDto(
    val id: Id<Comment, UUID>,
    val postId: Id<Article, UUID>,
    val parentId: Id<Comment, UUID>?,
    val writerId: Id<Member, UUID>,
    val content: String,
    val createdAt: LocalDateTime,
    val replyCount: Int
) {

    companion object {
        fun from(comment: Comment): CommentDto {
            return CommentDto(
                id = comment.id!!,
                postId = comment.articleId,
                parentId = comment.parentId,
                writerId = comment.writerId,
                content = comment.content,
                createdAt = comment.createdAt,
                replyCount = comment.replyCount
            )
        }
    }
}