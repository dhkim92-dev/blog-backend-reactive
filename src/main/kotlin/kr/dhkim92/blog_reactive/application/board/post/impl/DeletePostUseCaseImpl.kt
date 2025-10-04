package kr.dhkim92.blog_reactive.application.board.post.impl

import kr.dhkim92.blog_reactive.application.board.post.usecases.DeletePostUseCase
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
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
class DeletePostUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository
): DeletePostUseCase {

    override fun delete(loginId: Id<Member, UUID>, postId: Id<Article, UUID>): Mono<Void> {
        val memberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("유효하지 않은 사용자입니다.")))
        val articleMono = articleRepository.findById(postId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("유효하지 않은 게시글입니다.")))
        return Mono.zip(memberMono, articleMono)
            .flatMap { tuple ->
                val requester = tuple.t1
                val article = tuple.t2
                if (requester.id != article.writerId) {
                    return@flatMap Mono.error<Void>(IllegalAccessException("본인의 글만 삭제할 수 있습니다."))
                }
                // 1. 게시글 소프트 삭제 및 저장
                article.markDeleted()
                val articleSaveMono = articleRepository.save(article)

                // 2. 카테고리 카운트 감소 및 저장
                val categoryMono = categoryRepository.findById(article.categoryId)
                    .switchIfEmpty(Mono.error(IllegalArgumentException("유효하지 않은 카테고리입니다.")))
                    .map { category ->
                        category.decreaseCount()
                        category
                    }
                    .flatMap { categoryRepository.save(it) }
                Mono.`when`(articleSaveMono, categoryMono)
            }
    }
}