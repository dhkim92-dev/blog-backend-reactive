package kr.dhkim92.blog_reactive.adapter.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.board.mapper.CategoryMapper
import kr.dhkim92.blog_reactive.port.persistence.board.CategoryRepository
import kr.dhkim92.blog_reactive.port.persistence.board.r2dbc.R2dbcCategoryRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CategoryRepositoryImpl(
    private val r2dbcCategoryRepository: R2dbcCategoryRepository
): CategoryRepository {

    override fun save(category: Category): Mono<Category> {
        val entity = CategoryMapper.toEntity(category)
        return r2dbcCategoryRepository.save(entity)
            .map { savedEntity -> CategoryMapper.toDomain(savedEntity) }
    }

    override fun findById(id: Id<Category, Long>): Mono<Category> {
        return r2dbcCategoryRepository.findById(id.value)
            .map { entity -> CategoryMapper.toDomain(entity) }
    }

    override fun findByName(name: String): Mono<Category> {
        return r2dbcCategoryRepository.findByName(name)
            .map { entity -> CategoryMapper.toDomain(entity) }
    }

    override fun findAll(): Flux<Category> {
        return r2dbcCategoryRepository.findAll()
            .map { entity -> CategoryMapper.toDomain(entity) }
    }

    override fun delete(category: Category): Mono<Void> {
        val entity = CategoryMapper.toEntity(category)
        return r2dbcCategoryRepository.delete(entity)
    }

    override fun existsByName(name: String): Mono<Boolean> {
        return r2dbcCategoryRepository.existsByName(name)
    }
}