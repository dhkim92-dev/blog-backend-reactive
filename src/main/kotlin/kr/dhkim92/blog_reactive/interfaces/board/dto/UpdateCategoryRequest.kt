package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.application.board.category.dto.UpdateCategoryCommand
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import org.hibernate.validator.constraints.Length

@Schema(description = "Request to update an article category")
data class UpdateCategoryRequest(
    @field:Schema(description = "The new name for the category", example = "Tech News", required = true)
    @field: NotEmpty(message = "Category name must not be empty")
    @field: Length(max = 20, message = "Category name must not exceed 20 characters")
    val name: String
) {

    fun toCommand(categoryId: Long)
    = UpdateCategoryCommand(
        id = Id.of(
            Category::class,
            categoryId,
        ),
        name = this.name
    )
}