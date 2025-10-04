package kr.dhkim92.blog_reactive.application.board.category.impl

import kr.dhkim92.blog_reactive.application.board.category.dto.UpdateCategoryCommand
import kr.dhkim92.blog_reactive.application.board.category.usecases.UpdateCategoryUseCase
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.port.persistence.board.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional
class UpdateCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository
): UpdateCategoryUseCase {

    override fun update(command: UpdateCategoryCommand): Mono<Void> {
        val categoryMono = categoryRepository.findById(command.id)
            .switchIfEmpty(Mono.error(NotFoundException(message = "존재하지 않는 카테고리")))
        val existsMono = categoryRepository.existsByName(command.name)

        return Mono.zip(categoryMono, existsMono)
            .flatMap { tuple ->
                val category = tuple.t1
                val exists = tuple.t2
                if (exists && category.name != command.name) {
                    return@flatMap Mono.error<Void>(IllegalArgumentException("이미 존재하는 카테고리 이름"))
                }
                category.changeName(command.name)
                categoryRepository.save(category)
            }
            .then()
    }
}