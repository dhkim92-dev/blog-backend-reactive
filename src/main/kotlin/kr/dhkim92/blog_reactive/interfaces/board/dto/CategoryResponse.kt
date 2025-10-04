package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.common.response.BaseResponse

@Schema(description = "카테고리 응답")
data class CategoryResponse(
    @Schema(description = "카테고리 ID", example = "1")
    val id: Long,
    @Schema(description = "카테고리 이름", example = "Spring")
    val name: String,
    @Schema(description = "해당 카테고리에 속한 게시글 수", example = "10")
    val count: Int
): BaseResponse() {

    companion object{
        fun from(dto: CategoryDto) = CategoryResponse(
            id = dto.id,
            name = dto.name,
            count = dto.count
        )
    }
}