package kr.dhkim92.blog_reactive.adapter.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.board.CommentQueryModel
import kr.dhkim92.blog_reactive.domain.board.mapper.CommentMapper
import kr.dhkim92.blog_reactive.port.persistence.board.CommentRepository
import kr.dhkim92.blog_reactive.port.persistence.board.r2dbc.R2dbcCommentRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class CommentRepositoryImpl(
    private val r2dbcCommentRepository: R2dbcCommentRepository
) : CommentRepository {

    override fun save(comment: Comment): Mono<Comment> {
        val entity = CommentMapper.toEntity(comment)
        return r2dbcCommentRepository.save(entity)
            .map{ savedEntity -> CommentMapper.toDomain(savedEntity) }
    }

    override fun delete(comment: Comment): Mono<Void> {
        val entity = CommentMapper.toEntity(comment)
        return r2dbcCommentRepository.delete(entity)
    }

    override fun findById(id: Id<Comment, UUID>): Mono<Comment> {
        return r2dbcCommentRepository.findById(id.value)
            .map { CommentMapper.toDomain(it) }
    }

    override fun getByArticleIdAndCursorWithPagination(
        articleId: Id<Article, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModel> {
        return r2dbcCommentRepository.getByPostIdAndCursorPagination(
            postId = articleId.value,
            cursor = cursor?.value,
            size = size
        ).map { CommentQueryModel.from(it) }
    }

    override fun getByParentIdAndCursorWithPagination(
        parentId: Id<Comment, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModel> {
        return r2dbcCommentRepository.getByParentIdAndCursorPagination(
            parentId = parentId.value,
            cursor = cursor?.value,
            size = size
        ).map { CommentQueryModel.from(it) }
    }
}