package kr.dhkim92.blog_reactive.application.board.category.usecases

import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.application.board.category.dto.CreateCategoryCommand
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface CreateCategoryUseCase {

    fun create(loginId: Id<Member, UUID>, command: CreateCategoryCommand): Mono<CategoryDto>
}