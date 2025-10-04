package kr.dhkim92.blog_reactive.application.board.post.dto

import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID

data class PostCommandDto(
    val id: Id<Article, UUID>,
    val writerId: Id<Member, UUID>,
    val categoryId: Id<Category, Long>,
    val title: String,
    val contents: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isDeleted: Boolean,
) {

    companion object {
        fun from(entity: Article): PostCommandDto {
            return PostCommandDto(
                id = entity.id!!,
                writerId = entity.writerId,
                categoryId = entity.categoryId,
                title = entity.title,
                contents = entity.contents,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                isDeleted = entity.isDeleted
            )
        }
    }
}