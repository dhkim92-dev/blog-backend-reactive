package kr.dhkim92.blog_reactive.application.board.post.impl

import kr.dhkim92.blog_reactive.application.board.post.dto.UpdatePostCommand
import kr.dhkim92.blog_reactive.application.board.post.usecases.UpdatePostUseCase
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.board.ArticleRepository
import kr.dhkim92.blog_reactive.port.persistence.board.CategoryRepository
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional
class UpdatePostUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository
): UpdatePostUseCase {

    override fun update(
        loginId: Id<Member, UUID>,
        command: UpdatePostCommand
    ): Mono<Void> {
        val memberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(ForbiddenException(message="권한 없음")))
        val newCategoryMono = categoryRepository.findById(command.categoryId)
            .switchIfEmpty(Mono.error(NotFoundException(message = "유효하지 않은 카테고리")))
        val articleMono = articleRepository.findById(command.postId)
            .switchIfEmpty(Mono.error(NotFoundException(message = "유효하지 않은 게시글")))
        return Mono.zip(memberMono, newCategoryMono, articleMono)
            .flatMap { tuple ->
                val requester = tuple.t1
                val newCategory = tuple.t2
                val article = tuple.t3
                if (requester.id != article.writerId) {
                    return@flatMap Mono.error<Void>(ForbiddenException(message = "본인의 글만 수정할 수 있습니다."))
                }
                // 기존 카테고리 조회 및 decreaseCount
                val oldCategoryMono = categoryRepository.findById(article.categoryId)
                    .switchIfEmpty(Mono.error(NotFoundException(message = "기존 카테고리를 찾을 수 없습니다.")))
                    .map { oldCategory ->
                        oldCategory.decreaseCount()
                        oldCategory
                    }
                // 게시글 정보 변경
                article.updateTitle(requester,command.title)
                article.updateContents(requester,command.content)
                article.changeCategory(requester, newCategory)

                // 새 카테고리 increaseCount
                newCategory.increaseCount()

                // 저장
                val articleSaveMono = articleRepository.save(article)
                val newCategorySaveMono = categoryRepository.save(newCategory)
                val oldCategorySaveMono = oldCategoryMono.flatMap { categoryRepository.save(it) }

                Mono.`when`(articleSaveMono, newCategorySaveMono, oldCategorySaveMono)
            }
    }
}