package kr.dhkim92.blog_reactive.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.board.dto.CategoryResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateCategoryRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.UpdateCategoryRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name = "Article Category API", description = "게시물 카테고리 API")
@RequestMapping("/api/v1/article-categories")
interface CategoryApi {

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
    fun postCategory(
        loginId: Id<Member, UUID>,
        request: CreateCategoryRequest
    ): Mono<CategoryResponse>

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    fun putCategory(
        loginId: Id<Member, UUID>,
        categoryId: Long,
        request: UpdateCategoryRequest
    ): Mono<Void>

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "기존 카테고리를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공")
    fun deleteCategory(
        loginId: Id<Member, UUID>,
        categoryId: Long
    ): Mono<Void>

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    fun getCategories(): Mono<ListResponse<CategoryDto>>
}