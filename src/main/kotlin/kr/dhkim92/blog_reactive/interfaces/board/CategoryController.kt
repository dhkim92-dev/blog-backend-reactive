package kr.dhkim92.blog_reactive.interfaces.board

import jakarta.validation.Valid
import kr.dhkim92.blog_reactive.application.board.CategoryServiceFacade
import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.board.dto.CategoryResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateCategoryRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.UpdateCategoryRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class CategoryController(
    private val categoryServiceFacade: CategoryServiceFacade
): CategoryApi {

    @Envelope(status = HttpStatus.CREATED, message = "카테고리 생성 성공", code = "AC001")
    override fun postCategory(
        @Login loginId: Id<Member, UUID>,
        @RequestBody @Valid request: CreateCategoryRequest
    ): Mono<CategoryResponse> {
        return categoryServiceFacade.create(loginId, request.toCommand())
            .map { category -> CategoryResponse.from(category) }
    }

    @Envelope(status = HttpStatus.OK, message = "카테고리 목록 조회 성공", code = "AC004")
    override fun getCategories(): Mono<ListResponse<CategoryDto>> {
        return categoryServiceFacade.getCategories()
            .collectList()
            .map { categories -> ListResponse.from(items = categories) }
    }

    @Envelope(status = HttpStatus.OK, message = "카테고리 수정 성공", code = "AC002")
    override fun putCategory(
        @Login loginId: Id<Member, UUID>,
        @PathVariable categoryId: Long,
        @RequestBody @Valid request: UpdateCategoryRequest): Mono<Void> {
        return categoryServiceFacade.update(request.toCommand(categoryId))
    }

    @Envelope(status = HttpStatus.NO_CONTENT, message = "카테고리 삭제 성공", code = "AC003")
    override fun deleteCategory(
        @Login loginId: Id<Member, UUID>,
        @PathVariable categoryId: Long
    ): Mono<Void> {
        return categoryServiceFacade.delete(Id.of(Category::class, categoryId))
    }
}