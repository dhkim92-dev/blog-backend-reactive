package kr.dhkim92.blog_reactive.application.board.post.usecases

import kr.dhkim92.blog_reactive.application.board.post.dto.ArticleQueryModelDto
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface QueryPostUseCase {

    fun getPost(id: Id<Article, UUID>): Mono<ArticleQueryModelDto>

    fun getPostsByCategoryIdAndCursorIdPagination(
        categoryId: Id<Category, Long>?,
        cursorId: Id<Article, UUID>?,
        size: Int
    ): Flux<ArticleQueryModelDto>
}