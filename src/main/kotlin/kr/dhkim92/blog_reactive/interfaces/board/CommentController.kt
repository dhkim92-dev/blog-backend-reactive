package kr.dhkim92.blog_reactive.interfaces.board

import jakarta.validation.Valid
import kr.dhkim92.blog_reactive.application.board.CommentServiceFacade
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.board.dto.CommentQueryResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CommentResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateCommentRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateReplyRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.UpdateCommentRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class CommentController(
    private val commentServiceFacade: CommentServiceFacade
): CommentApi {

    override fun postComment(
        @Login loginId: Id<Member, UUID>,
        @PathVariable postId: UUID,
        @RequestBody @Valid request: CreateCommentRequest
    ): Mono<CommentResponse> {
        return commentServiceFacade.createComment(
            loginId,
            request.toCommand(postId)
        ).map { comment -> CommentResponse.from(comment) }
    }

    override fun postReply(
        @Login loginId: Id<Member, UUID>,
        @PathVariable commentId: UUID,
        @RequestBody @Valid request: CreateReplyRequest
    ): Mono<CommentResponse> {
        return commentServiceFacade.createReply(
            loginId,
            request.toCommand(commentId)
        ).map { comment -> CommentResponse.from(comment) }
    }

    override fun putComment(
        @Login loginId: Id<Member, UUID>,
        @PathVariable commentId: UUID,
        @RequestBody @Valid request: UpdateCommentRequest
    ): Mono<Void> {
        return commentServiceFacade.updateComment(
            loginId,
            request.toCommand(commentId)
        )
    }

    override fun deleteComment(
        @Login loginId: Id<Member, UUID>,
        @PathVariable commentId: UUID
    ): Mono<Void> {
        return commentServiceFacade.deleteComment(
            loginId,
            Id.of(Comment::class, commentId)
        )
    }

    override fun getPostComments(
        @PathVariable postId: UUID,
        @RequestParam(required=false) cursor: UUID?,
        @RequestParam(defaultValue="20") size: Int
    ): Mono<ListResponse<CommentQueryResponse>> {
        return commentServiceFacade.getPostComments(
            articleId = Id.of(Article::class, postId),
            cursor = cursor?.let { Id.of(Comment::class, it) },
            size = size + 1
        )
        .map { CommentQueryResponse.from(it) }
        .collectList()
        .flatMap { items ->
            ListResponse.ofAsync(
                size = size,
                items = items,
                extractors = buildMap {
                    put("cursor") { it.id }
                }
            )
        }
    }

    override fun getCommentReplies(
        @PathVariable commentId: UUID,
        @RequestParam(required = false) cursor: UUID?,
        @RequestParam(defaultValue = "20") size: Int
    ): Mono<ListResponse<CommentQueryResponse>> {
        return commentServiceFacade.getCommentReplies(
            commentId = Id.of(Comment::class, commentId),
            cursor = cursor?.let { Id.of(Comment::class, it) },
            size = size + 1
        )
        .map { CommentQueryResponse.from(it) }
        .collectList()
        .flatMap { items ->
            ListResponse.ofAsync(
                size = size,
                items = items,
                extractors = buildMap {
                    put("cursor") { it.id }
                }
            )
        }
    }
}