package kr.dhkim92.blog_reactive.adapter.persistence

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.ArticleQueryModel
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.board.mapper.ArticleMapper
import kr.dhkim92.blog_reactive.port.persistence.board.ArticleRepository
import kr.dhkim92.blog_reactive.port.persistence.board.r2dbc.R2dbcArticleRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class ArticleRepositoryImpl(
    private val r2dbcArticleRepository: R2dbcArticleRepository
) : ArticleRepository {

    override fun save(article: Article): Mono<Article> {
        val entity = ArticleMapper.toEntity(article)
        return r2dbcArticleRepository.save(entity)
            .log()
            .map{ savedEntity -> ArticleMapper.toDomain(savedEntity) }
    }

    override fun findById(id: Id<Article, UUID>): Mono<Article> {
        return r2dbcArticleRepository.findById(id.value)
            .map { ArticleMapper.toDomain(it) }
    }

    override fun delete(article: Article): Mono<Void> {
        val entity = ArticleMapper.toEntity(article)
        return r2dbcArticleRepository.delete(entity)
    }

    override fun getPostById(articleId: Id<Article, UUID>)
    : Mono<ArticleQueryModel> {
        return r2dbcArticleRepository.getById(articleId.value)
            .doOnNext { println("쿼리 결과: $it") }
            .doOnError { println("쿼리 에러: $it") }
            .map { it.toArticleQueryModel() }
    }

    override fun getPostsByCategoryIdAndCursorIdPagination(
        categoryId: Id<Category, Long>?,
        cursor: Id<Article, UUID>?,
        size: Int
    ): Flux<ArticleQueryModel> {
        return r2dbcArticleRepository.getByCategoryIdAndCursorIdPagination(
            categoryId?.value,
            cursor?.value,
            size
        ).map { it.toArticleQueryModel() }
    }
}