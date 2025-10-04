package kr.dhkim92.blog_reactive.port.persistence.board.r2dbc

import kr.dhkim92.blog_reactive.domain.board.r2dbc.CommentEntity
import kr.dhkim92.blog_reactive.domain.board.r2dbc.CommentQueryRecord
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface R2dbcCommentRepository : R2dbcRepository<CommentEntity, UUID>{

    @Query("""
        SELECT
            c.id AS commentId,
            c.content AS commentContent,
            c.parent_id AS parentId,
            c.article_id AS articleId,
            m.id AS writerId,
            m.nickname AS writerNickname,
            c.reply_count as replyCount,
            c.created_at AS commentCreatedAt,
            c.updated_at AS commentUpdatedAt
        FROM article_comment c 
        JOIN member m on c.member_id = m.id
        WHERE c.post_id = :postId
        AND (:cursor IS NULL OR c.id < :cursor)
        AND c.is_deleted = FALSE
        ORDER BY c.id DESC
        LIMIT :size
    """)
    fun getByPostIdAndCursorPagination(
        postId: UUID,
        cursor: UUID?,
        size: Int
    ): Flux<CommentQueryRecord>

    @Query(
        """
        SELECT
            c.id AS commentId,
            c.parent_id AS parentId,
            c.article_id AS articleId,
            m.id AS writerId,
            m.nickname AS writerNickname,
            c.content AS commentContent,
            c.reply_count as replyCount,
            c.created_at AS commentCreatedAt,
            c.updated_at AS commentUpdatedAt
        FROM article_comment c 
        JOIN member m on c.member_id = m.id
        WHERE c.parent_id = :parentId
        AND (:cursor IS NULL OR c.id < :cursor)
        AND c.is_deleted = FALSE
        ORDER BY c.id DESC
        LIMIT :size
    """)
    fun getByParentIdAndCursorPagination(
        parentId: UUID,
        cursor: UUID?,
        size: Int
    ): Flux<CommentQueryRecord>
}