package kr.dhkim92.blog_reactive.domain.board.mapper

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.board.r2dbc.CategoryEntity

object CategoryMapper {

    fun toDomain(entity: CategoryEntity): Category {
        return Category(
            id = Id.of(Category::class, entity.id!!),
            name = entity.name,
            count = entity.count
        )
    }

    fun toEntity(domain: Category): CategoryEntity {
        return CategoryEntity(
            id = domain.id?.value,
            name = domain.name,
            count = domain.count
        )
    }
}