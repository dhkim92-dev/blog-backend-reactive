package kr.dhkim92.blog_reactive.port.persistence.board

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.ArticleQueryModel
import kr.dhkim92.blog_reactive.domain.board.Category
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface ArticleRepository {

    fun save(article: Article): Mono<Article>

    fun findById(id: Id<Article, UUID>): Mono<Article>

    fun delete(article: Article): Mono<Void>

    fun getPostById(articleId: Id<Article, UUID>): Mono<ArticleQueryModel>

    fun getPostsByCategoryIdAndCursorIdPagination(
        categoryId: Id<Category, Long>?,
        cursor: Id<Article, UUID>?,
        size: Int
    ): Flux<ArticleQueryModel>
}