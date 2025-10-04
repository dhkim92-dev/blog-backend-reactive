package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.post.dto.PostCommandDto
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "게시글 명령 응답")
data class ArticleCommandResponse(
    val id: UUID,
    val writerId: UUID,
    val categoryId: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isDeleted: Boolean,
) {

    companion object {
        fun from(dto: PostCommandDto): ArticleCommandResponse {
            return ArticleCommandResponse(
                id = dto.id.value,
                writerId = dto.writerId.value,
                categoryId = dto.categoryId.value,
                title = dto.title,
                content = dto.contents,
                createdAt = dto.createdAt,
                isDeleted = dto.isDeleted
            )
        }
    }
}