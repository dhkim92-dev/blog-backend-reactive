package kr.dhkim92.blog_reactive.interfaces.board.dto

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentQueryModelDto
import kr.dhkim92.blog_reactive.common.response.BaseResponse
import java.time.LocalDateTime
import java.util.UUID

data class CommentQueryResponse(
    val id: UUID,
    val parentId: UUID?,
    val writer: WriterResponse,
    val content: String,
    val createdAt: LocalDateTime,
    val replyCount: Int
): BaseResponse() {

    companion object {
        fun from(dto: CommentQueryModelDto): CommentQueryResponse {
            return CommentQueryResponse(
                id = dto.id.value,
                parentId = dto.parentId?.value,
                writer = WriterResponse(
                    id = dto.writer.id.value,
                    nickname = dto.writer.nickname
                ),
                content = dto.content,
                createdAt = dto.createdAt,
                replyCount = dto.replyCount
            )
        }
    }
}