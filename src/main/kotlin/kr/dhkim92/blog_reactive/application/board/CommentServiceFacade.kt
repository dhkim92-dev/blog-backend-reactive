package kr.dhkim92.blog_reactive.application.board

import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateCommentCommand
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateReplyCommand
import kr.dhkim92.blog_reactive.application.board.comment.dto.UpdateCommentCommand
import kr.dhkim92.blog_reactive.application.board.comment.usecases.CreateCommentUseCase
import kr.dhkim92.blog_reactive.application.board.comment.usecases.CreateReplyUseCase
import kr.dhkim92.blog_reactive.application.board.comment.usecases.DeleteCommentUseCase
import kr.dhkim92.blog_reactive.application.board.comment.usecases.QueryCommentUseCase
import kr.dhkim92.blog_reactive.application.board.comment.usecases.UpdateCommentUseCase
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.member.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CommentServiceFacade(
    private val createCommentUseCase: CreateCommentUseCase,
    private val createReplyUseCase: CreateReplyUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val queryCommentUseCase: QueryCommentUseCase
) {

    fun createComment(
        loginId: Id<Member, UUID>,
        command: CreateCommentCommand
    ) = createCommentUseCase.create(loginId, command)

    fun createReply(
        loginId: Id<Member, UUID>,
        command: CreateReplyCommand
    ) = createReplyUseCase.createReply(loginId, command)

    fun updateComment(
        loginId: Id<Member, UUID>,
        command: UpdateCommentCommand
    ) = updateCommentUseCase.update(loginId, command)

    fun deleteComment(
        loginId: Id<Member, UUID>,
        commentId: Id<Comment, UUID>
    ) = deleteCommentUseCase.delete(loginId, commentId)

    fun getPostComments(
        articleId: Id<Article, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ) = queryCommentUseCase.getPostComments(
        postId = articleId,
        cursor = cursor,
        size = size
    )

    fun getCommentReplies(
        commentId: Id<Comment, UUID>,
        cursor: Id<Comment, UUID>?,
        size: Int
    ) = queryCommentUseCase.getCommentReplies(
        commentId = commentId,
        cursor = cursor,
        size = size
    )
}