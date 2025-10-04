package kr.dhkim92.blog_reactive.domain.board.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "article")
class ArticleEntity(
    @Id
    var id: UUID? = null,
    @Column("member_id")
    var memberId: UUID = UUID.randomUUID(),
    @Column("category_id")
    var categoryId: Long = 0L,
    @Column("title")
    var title: String = "",
    @Column("contents")
    var contents: String = "",
    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column("is_deleted")
    var isDeleted: Boolean = false
) {

}