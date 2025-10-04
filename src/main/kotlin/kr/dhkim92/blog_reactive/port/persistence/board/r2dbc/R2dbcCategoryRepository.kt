package kr.dhkim92.blog_reactive.port.persistence.board.r2dbc

import kr.dhkim92.blog_reactive.domain.board.r2dbc.CategoryEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface R2dbcCategoryRepository : R2dbcRepository<CategoryEntity, Long> {

    fun findByName(name: String): Mono<CategoryEntity>

    fun existsByName(name: String): Mono<Boolean>
}