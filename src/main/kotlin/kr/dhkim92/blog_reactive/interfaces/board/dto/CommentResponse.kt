package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentDto
import kr.dhkim92.blog_reactive.common.response.BaseResponse
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "댓글 응답")
data class CommentResponse(
    @Schema(description = "댓글 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val id: UUID,
    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", nullable = true)
    val parentId: UUID?,
    @Schema(description = "게시글 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val articleId: UUID,
    @Schema(description = "작성자 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val writerId: UUID,
    @Schema(description = "댓글 본문", example = "댓글 내용입니다.")
    val content: String,
    @Schema(description = "댓글 작성일", example = "2023-10-01T12:34:56")
    val createdAt: LocalDateTime,
    @Schema(description = "답글 수", example = "5")
    val replyCount: Int
) : BaseResponse(){

    companion object {
        fun from(dto: CommentDto): CommentResponse {
            return CommentResponse(
                id = dto.id.value,
                parentId = dto.parentId?.value,
                articleId = dto.writerId.value,
                writerId = dto.writerId.value,
                content = dto.content,
                createdAt = dto.createdAt,
                replyCount = dto.replyCount
            )
        }
    }
}