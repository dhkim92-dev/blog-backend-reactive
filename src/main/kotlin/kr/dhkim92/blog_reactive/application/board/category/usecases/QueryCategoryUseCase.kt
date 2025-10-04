package kr.dhkim92.blog_reactive.application.board.category.usecases

import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import reactor.core.publisher.Flux

interface QueryCategoryUseCase {

    fun getCategories(): Flux<CategoryDto>
}