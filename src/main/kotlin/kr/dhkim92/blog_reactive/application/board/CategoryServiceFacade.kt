package kr.dhkim92.blog_reactive.application.board

import kr.dhkim92.blog_reactive.application.board.category.dto.CreateCategoryCommand
import kr.dhkim92.blog_reactive.application.board.category.dto.UpdateCategoryCommand
import kr.dhkim92.blog_reactive.application.board.category.usecases.CreateCategoryUseCase
import kr.dhkim92.blog_reactive.application.board.category.usecases.DeleteCategoryUseCase
import kr.dhkim92.blog_reactive.application.board.category.usecases.QueryCategoryUseCase
import kr.dhkim92.blog_reactive.application.board.category.usecases.UpdateCategoryUseCase
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryServiceFacade(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val queryCategoryUseCase: QueryCategoryUseCase,
) {

    fun create(loginId: Id<Member, UUID>, command: CreateCategoryCommand) = createCategoryUseCase.create(loginId, command)

    fun getCategories() = queryCategoryUseCase.getCategories()

    fun update(command: UpdateCategoryCommand) = updateCategoryUseCase.update(command)

    fun delete(id: Id<Category, Long>) = deleteCategoryUseCase.delete(id)
}