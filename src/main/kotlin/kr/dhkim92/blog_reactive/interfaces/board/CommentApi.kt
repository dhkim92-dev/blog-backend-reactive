package kr.dhkim92.blog_reactive.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateCommentCommand
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.board.dto.CommentQueryResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CommentResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateCommentRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateReplyRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.UpdateCommentRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name = "Comment", description = "댓글 API")
@RequestMapping("/api/v1")
interface CommentApi {

    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    @PostMapping("/articles/{postId}/comments")
    @Envelope(status = HttpStatus.CREATED, message = "댓글 작성 성공", code = "C001")
    fun postComment(
        loginId: Id<Member, UUID>,
        postId: UUID,
        request: CreateCommentRequest
    ): Mono<CommentResponse>

    @Operation(summary = "대댓글 작성", description = "댓글에 대댓글을 작성합니다.")
    @PostMapping("/comments/{commentId}/replies")
    @ApiResponse(responseCode = "201", description = "대댓글 작성 성공")
    @Envelope(status = HttpStatus.CREATED, message = "대댓글 작성 성공", code = "C002")
    fun postReply(
        loginId: Id<Member, UUID>,
        commentId: UUID,
        request: CreateReplyRequest
    ): Mono<CommentResponse>

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/comments/{commentId}")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    @Envelope(status = HttpStatus.OK, message = "댓글 수정 성공", code = "C003")
    fun putComment(
        loginId: Id<Member, UUID>,
        commentId: UUID,
        request: UpdateCommentRequest
    ): Mono<Void>

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공")
    @Envelope(status = HttpStatus.NO_CONTENT, message = "댓글 삭제 성공", code = "C004")
    fun deleteComment(
        loginId: Id<Member, UUID>,
        commentId: UUID,
    ): Mono<Void>

    @Operation(summary = "게시글 댓글 조회", description = "게시글의 댓글 목록을 조회합니다.")
    @GetMapping("/articles/{postId}/comments")
    @Envelope(status = HttpStatus.OK, message = "댓글 목록 조회 성공", code = "C005")
    @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")
    fun getPostComments(
        postId: UUID,
        cursor: UUID?,
        size: Int
    ): Mono<ListResponse<CommentQueryResponse>>

    @Operation(summary = "댓글 대댓글 조회", description = "댓글의 대댓글 목록을 조회합니다.")
    @GetMapping("/comments/{commentId}/replies")
    @Envelope(status = HttpStatus.OK, message = "대댓글 목록 조회 성공", code = "C006")
    @ApiResponse(responseCode = "200", description = "대댓글 목록 조회 성공")
    fun getCommentReplies(
        commentId: UUID,
        cursor: UUID?,
        size: Int
    ): Mono<ListResponse<CommentQueryResponse>>
}