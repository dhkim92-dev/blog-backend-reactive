package kr.dhkim92.blog_reactive.application.board.category.impl

import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.application.board.category.usecases.QueryCategoryUseCase
import kr.dhkim92.blog_reactive.port.persistence.board.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

@Service
@Transactional(readOnly = true)
class QueryCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository
): QueryCategoryUseCase {

    override fun getCategories(): Flux<CategoryDto> {
        return categoryRepository.findAll()
            .map { category -> CategoryDto.from(category) }
    }
}