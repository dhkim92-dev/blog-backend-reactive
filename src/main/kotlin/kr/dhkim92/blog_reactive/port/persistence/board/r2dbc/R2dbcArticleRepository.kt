package kr.dhkim92.blog_reactive.port.persistence.board.r2dbc

import kr.dhkim92.blog_reactive.domain.board.r2dbc.ArticleEntity
import kr.dhkim92.blog_reactive.domain.board.r2dbc.ArticleQueryRecord
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface R2dbcArticleRepository : R2dbcRepository<ArticleEntity, UUID> {

    @Query("""
        SELECT 
            a.id AS articleId,
            a.title AS articleTitle,
            a.contents AS articleContents,
            m.id AS writerId,
            m.nickname AS writerNickname,
            c.id AS categoryId,
            c.name AS categoryName,
            c.count AS categoryCount,
            a.created_at AS articleCreatedAt,
            a.updated_at AS articleUpdatedAt
        FROM article a
        JOIN member m ON a.member_id = m.id
        JOIN article_category c ON a.category_id = c.id
        WHERE a.id = :id AND a.is_deleted = FALSE
    """)
    fun getById(id: UUID): Mono<ArticleQueryRecord>

    @Query("""
        SELECT 
            a.id AS articleId,
            a.title AS articleTitle,
            '' AS articleContents,
            m.id AS writerId,
            m.nickname AS writerNickname,
            c.id AS categoryId,
            c.name AS categoryName,
            c.count AS categoryCount,
            a.created_at AS articleCreatedAt,
            a.updated_at AS articleUpdatedAt
        FROM article a
        LEFT JOIN member m ON a.member_id = m.id
        LEFT JOIN article_category c ON a.category_id = c.id
        WHERE a.category_id IS NOT NULL
        AND (
            (:categoryId IS NULL AND c.id IS NOT NULL)
            OR c.id = :categoryId
        )
        AND (:cursor IS NULL OR a.id < :cursor)
        AND a.is_deleted = FALSE
        ORDER BY a.id DESC
        LIMIT :size
    """)
    fun getByCategoryIdAndCursorIdPagination(
        categoryId: Long?,
        cursor: UUID?,
        size: Int
    ): Flux<ArticleQueryRecord>
}