package kr.dhkim92.blog_reactive.domain.board

import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.r2dbc.CommentQueryRecord
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

class CommentQueryModel(
    val id: Id<Comment, UUID>,
    val articleId: Id<Article, UUID>,
    val parentId: Id<Comment, UUID>?,
    val writer: Writer,
    val content: String,
    val replyCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    companion object {
        fun from(record: CommentQueryRecord): CommentQueryModel {
            return CommentQueryModel(
                id = Id.of(Comment::class, record.id),
                articleId = Id.of(Article::class, record.articleid),
                parentId = record.parentid?.let { Id.of(Comment::class, it) },
                writer = Writer(
                    id = Id.of(Member::class, record.writerid),
                    nickname = record.writername
                ),
                content = record.content,
                replyCount = record.replycount,
                createdAt = record.createdat,
                updatedAt = record.updatedat,
            )
        }
    }
}