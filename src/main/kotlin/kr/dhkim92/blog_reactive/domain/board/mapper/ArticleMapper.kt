package kr.dhkim92.blog_reactive.domain.board.mapper

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.board.r2dbc.ArticleEntity
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.r2dbc.MemberEntity

object ArticleMapper {

    fun toDomain(entity: ArticleEntity): Article {
        return Article(
            id = Id.of(Article::class, entity.id!!),
            writerId = Id.of(Member::class, entity.memberId),
            categoryId = Id.of(Category::class, entity.categoryId),
            title = entity.title,
            contents = entity.contents,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted
        )
    }

    fun toEntity(domain: Article): ArticleEntity {
        return ArticleEntity(
            id = domain.id?.value ?: null,
            memberId = domain.writerId.value,
            categoryId = domain.categoryId.value,
            title = domain.title,
            contents = domain.contents,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            isDeleted = domain.isDeleted
        )
    }
}