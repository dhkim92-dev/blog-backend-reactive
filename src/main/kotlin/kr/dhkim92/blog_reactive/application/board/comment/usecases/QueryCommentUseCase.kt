package kr.dhkim92.blog_reactive.application.board.comment.usecases

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentDto
import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentQueryModelDto
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import reactor.core.publisher.Flux
import java.util.UUID

interface QueryCommentUseCase {

    fun getPostComments(
        postId: Id<Article, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModelDto>

    fun getCommentReplies(
        commentId: Id<Comment, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ): Flux<CommentQueryModelDto>
}