package kr.dhkim92.blog_reactive.port.persistence.board

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.board.CommentQueryModel
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface CommentRepository {

    // Commands

    fun save(comment: Comment): Mono<Comment>

    fun findById(id: Id<Comment, UUID>): Mono<Comment>

    fun delete(comment: Comment): Mono<Void>

    // Queries

    fun getByArticleIdAndCursorWithPagination(
        articleId: Id<Article, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModel>

    fun getByParentIdAndCursorWithPagination(
        parentId: Id<Comment, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModel>
}