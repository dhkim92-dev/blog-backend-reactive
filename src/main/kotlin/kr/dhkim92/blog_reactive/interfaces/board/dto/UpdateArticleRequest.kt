package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.application.board.post.dto.UpdatePostCommand
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import java.util.UUID

@Schema(description = "게시글 수정 요청")
class UpdateArticleRequest(
    @field:Schema(description = "카테고리 ID", example = "1")
    @field: Min(value = 1, message = "카테고리 ID는 1 이상의 값이어야 합니다.")
    val categoryId: Long,
    @field:Schema(description = "제목", example = "첫 번째 게시글")
    @field: NotEmpty(message = "제목은 필수입니다.")
    val title: String,
    @field:Schema(description = "내용", example = "안녕하세요, 첫 번째 게시글입니다.")
    @field:NotEmpty(message = "내용은 필수입니다.")
    val content: String,
) {

    fun toCommand(
        articleId: UUID,
    ): UpdatePostCommand {
        return UpdatePostCommand(
            postId = Id.of(Article::class, articleId),
            categoryId = Id.of(Category::class, categoryId),
            title = title,
            content = content
        )
    }
}