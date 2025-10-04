package kr.dhkim92.blog_reactive.application.board.category.usecases

import kr.dhkim92.blog_reactive.application.board.category.dto.UpdateCategoryCommand
import reactor.core.publisher.Mono

interface UpdateCategoryUseCase {

    fun update(command: UpdateCategoryCommand): Mono<Void>
}