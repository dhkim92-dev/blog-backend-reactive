package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.application.board.category.dto.CreateCategoryCommand

@Schema(description = "카테고리 생성 요청")
data class CreateCategoryRequest(
//    @field:Schema(description = "카테고리 이름", example = "Spring")
//    @field:NotEmpty
    val name: String
) {

    fun toCommand() = CreateCategoryCommand(name = name)
}