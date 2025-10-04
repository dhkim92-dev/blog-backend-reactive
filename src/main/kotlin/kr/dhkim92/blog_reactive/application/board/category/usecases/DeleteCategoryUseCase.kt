package kr.dhkim92.blog_reactive.application.board.category.usecases

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import reactor.core.publisher.Mono

interface DeleteCategoryUseCase {

    fun delete(id: Id<Category, Long>): Mono<Void>
}