package kr.dhkim92.blog_reactive.port.persistence.board

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CategoryRepository {

    fun save(category: Category): Mono<Category>

    fun findById(id: Id<Category, Long>): Mono<Category>

    fun findByName(name: String): Mono<Category>

    fun findAll(): Flux<Category>

    fun existsByName(name: String): Mono<Boolean>

    fun delete(category: Category): Mono<Void>
}