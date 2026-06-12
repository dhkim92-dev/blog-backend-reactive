package kr.dhkim92.blog_reactive.application.board.comment.impl

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentQueryModelDto
import kr.dhkim92.blog_reactive.application.board.comment.usecases.QueryCommentUseCase
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.port.persistence.board.CommentRepository
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import java.util.UUID

@Service
@Transactional(readOnly = true)
class QueryCommentUseCaseImpl(
    private val commentRepository: CommentRepository
): QueryCommentUseCase {

    override fun getPostComments(
        postId: Id<Article, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModelDto> {
        return commentRepository.getByArticleIdAndCursorWithPagination(
            articleId = postId,
            cursor = cursor,
            size = size
        ).map {
            comment -> CommentQueryModelDto.from(comment)
        }
    }

    override fun getCommentReplies(
        commentId: Id<Comment, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModelDto> {
        return commentRepository.getByParentIdAndCursorWithPagination(
            parentId = commentId,
            cursor = cursor,
            size = size
        ).map { comment -> CommentQueryModelDto.from(comment) }
    }
}