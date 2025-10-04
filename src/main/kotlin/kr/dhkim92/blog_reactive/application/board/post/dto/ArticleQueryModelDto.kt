package kr.dhkim92.blog_reactive.application.board.post.dto

import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.domain.board.ArticleQueryModel
import java.time.LocalDateTime
import java.util.UUID

data class ArticleQueryModelDto(
    val id: UUID,
    val title: String,
    val content: String,
    val writer: Writer,
    val category: CategoryDto,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val viewCount: Long = 0L,
    val likeCount: Long = 0L,
    val commentCount: Long = 0L
) {

    companion object {
        fun from(model: ArticleQueryModel): ArticleQueryModelDto {
            return ArticleQueryModelDto(
                id = model.id.value,
                title = model.title,
                content = model.content,
                writer = model.writer,
                category = CategoryDto.from(model.category),
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                viewCount = model.viewCount,
                likeCount = model.likeCount,
                commentCount = model.commentCount
            )
        }
    }
}