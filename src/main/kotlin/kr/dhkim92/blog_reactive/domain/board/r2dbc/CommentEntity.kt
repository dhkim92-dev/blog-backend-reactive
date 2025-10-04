package kr.dhkim92.blog_reactive.domain.board.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "article_comment")
class CommentEntity(
    @Id
    var id: UUID? = null,
    @Column("article_id")
    var articleId: UUID,
    @Column("parent_id")
    var parentId: UUID?,
    @Column("member_id")
    var memberId: UUID,
    @Column("content")
    var content: String,
    @Column("created_at")
    var createdAt: LocalDateTime,
    @Column("updated_at")
    var updatedAt: LocalDateTime,
    @Column("is_deleted")
    var isDeleted: Boolean = false,
    @Column("reply_count")
    var replyCount: Int = 0,
    @Column("depth")
    var depth: Int = 0
) {
}