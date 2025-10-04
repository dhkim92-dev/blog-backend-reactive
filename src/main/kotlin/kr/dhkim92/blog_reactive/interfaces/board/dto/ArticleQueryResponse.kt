package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.post.dto.ArticleQueryModelDto
import kr.dhkim92.blog_reactive.common.response.BaseResponse
import java.time.LocalDateTime

@Schema(description = "게시글 조회 응답")
class ArticleQueryResponse(
    @Schema(description = "게시글 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val id: String,
    @Schema(description = "게시글 제목", example = "Spring Boot로 게시판 만들기")
    val title: String,
    @Schema(description = "게시글 내용", example = "Spring Boot와 Kotlin으로 게시판을 만들어봅시다...")
    val content: String,
    val writer: WriterResponse,
    val category: CategoryResponse,
    @Schema(description = "게시글 생성일", example = "2023-10-01T12:34:56")
    val createdAt: LocalDateTime,
    @Schema(description = "게시글 수정일", example = "2023-10-02T14:20:00")
    val updatedAt: LocalDateTime,
    @Schema(description = "조회수", example = "100")
    val viewCount: Long,
    @Schema(description = "좋아요 수", example = "25")
    val likeCount: Long,
    @Schema(description = "댓글 수", example = "10")
    val commentCount: Long
): BaseResponse() {

    companion object {

        fun from(modelDto: ArticleQueryModelDto)
        : ArticleQueryResponse {
            return ArticleQueryResponse(
                id = modelDto.id.toString(),
                title = modelDto.title,
                content = modelDto.content,
                writer = WriterResponse.from(modelDto.writer),
                category = CategoryResponse.from(modelDto.category),
                createdAt = modelDto.createdAt,
                updatedAt = modelDto.updatedAt,
                viewCount = modelDto.viewCount,
                likeCount = modelDto.likeCount,
                commentCount = modelDto.commentCount
            )
        }
    }
}