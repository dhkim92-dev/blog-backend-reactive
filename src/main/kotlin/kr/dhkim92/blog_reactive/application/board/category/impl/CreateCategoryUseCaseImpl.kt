package kr.dhkim92.blog_reactive.application.board.category.impl

import kr.dhkim92.blog_reactive.application.board.category.dto.CategoryDto
import kr.dhkim92.blog_reactive.application.board.category.dto.CreateCategoryCommand
import kr.dhkim92.blog_reactive.application.board.category.usecases.CreateCategoryUseCase
import kr.dhkim92.blog_reactive.common.error.ConflictException
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.board.CategoryRepository
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional
class CreateCategoryUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository
): CreateCategoryUseCase {

    override fun create(loginId: Id<Member, UUID>, command: CreateCategoryCommand): Mono<CategoryDto> {
        return categoryRepository.existsByName(command.name)
            .flatMap { exists ->
                if (exists) {
                    Mono.error(ConflictException(message = "이미 존재하는 카테고리 이름입니다."))
                } else {
                    memberRepository.findById(loginId)
                        .switchIfEmpty(Mono.error(NotFoundException(message = "존재하지 않는 회원입니다.")))
                        .flatMap { member ->
                            val category = Category(name = command.name)
                            categoryRepository.save(category)
                        }
                        .map { savedCategory -> CategoryDto.from(savedCategory) }
                }
            }
    }
}