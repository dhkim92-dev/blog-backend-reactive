package kr.dhkim92.blog_reactive.application.board.post.impl

import kr.dhkim92.blog_reactive.application.board.post.dto.ArticleQueryModelDto
import kr.dhkim92.blog_reactive.application.board.post.usecases.QueryPostUseCase
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.port.persistence.board.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional(readOnly = true)
class QueryPostUseCaseImpl(
    private val articleRepository: ArticleRepository
): QueryPostUseCase {

    override fun getPost(id: Id<Article, UUID>): Mono<ArticleQueryModelDto> {
        return articleRepository.getPostById(id)
            .switchIfEmpty(Mono.error(NotFoundException("존재하지 않는 게시물입니다.")))
            .map { article -> ArticleQueryModelDto.from(article) }
    }

    override fun getPostsByCategoryIdAndCursorIdPagination(
        categoryId: Id<Category, Long>?,
        cursorId: Id<Article, UUID>?,
        size: Int
    ): Flux<ArticleQueryModelDto> {
        return articleRepository.getPostsByCategoryIdAndCursorIdPagination(
            categoryId = categoryId,
            cursor = cursorId,
            size = size
        ).map { article -> ArticleQueryModelDto.from(article) }
    }
}