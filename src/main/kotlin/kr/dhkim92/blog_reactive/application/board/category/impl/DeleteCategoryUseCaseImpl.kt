package kr.dhkim92.blog_reactive.application.board.category.impl

import kr.dhkim92.blog_reactive.application.board.category.usecases.DeleteCategoryUseCase
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.port.persistence.board.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional
class DeleteCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository
): DeleteCategoryUseCase {

    override fun delete(id: Id<Category, Long>): Mono<Void> {
        return categoryRepository.findById(id)
            .flatMap { category -> categoryRepository.delete(category) }
    }
}