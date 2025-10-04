package kr.dhkim92.blog_reactive.application.board.post.impl

import kr.dhkim92.blog_reactive.application.board.post.dto.CreatePostCommand
import kr.dhkim92.blog_reactive.application.board.post.dto.PostCommandDto
import kr.dhkim92.blog_reactive.application.board.post.usecases.CreatePostUseCase
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
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
class CreatePostUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository
): CreatePostUseCase {

    override fun create(loginId: Id<Member, UUID>, command: CreatePostCommand): Mono<PostCommandDto> {
        val memberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(ForbiddenException("권한 없음")))
        val categoryMono = categoryRepository.findById(command.categoryId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("유효하지 않은 카테고리입니다.")))
        return Mono.zip(memberMono, categoryMono)
            .map { tuple ->
                val requester = tuple.t1
                val category = tuple.t2
                val article = Article.create(
                    requester = requester,
                    category = category,
                    command = command
                )
                category.increaseCount()
                Pair(article, category)
            }.flatMap { pair ->
                val articleSaveMono = articleRepository.save(pair.first)
                val categorySaveMono = categoryRepository.save(pair.second)
                Mono.zip(articleSaveMono, categorySaveMono)
                    .map { tuple ->
                        PostCommandDto.from(tuple.t1)
                    }
            }
    }
}