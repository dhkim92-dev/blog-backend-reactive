package kr.dhkim92.blog_reactive.application.board.comment.impl

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentDto
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateCommentCommand
import kr.dhkim92.blog_reactive.application.board.comment.usecases.CreateCommentUseCase
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.board.ArticleRepository
import kr.dhkim92.blog_reactive.port.persistence.board.CommentRepository
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.util.UUID

@Service
@Transactional
class CreateCommentUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository
): CreateCommentUseCase {

    override fun create(loginId: Id<Member, UUID>, command: CreateCommentCommand): Mono<CommentDto> {
        val member = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(ForbiddenException(message = "권한 없음")))
        val article = articleRepository.findById(command.postId)
            .switchIfEmpty(Mono.error(NotFoundException(message="존재하지 않는 게시글")))

        return Mono.zip(member, article)
            .flatMap { tuple ->
                val (m, a) = tuple
                val comment = Comment.create(
                    article = a,
                    writer = m,
                    parent = null,
                    content = command.content,
                )
                commentRepository.save(comment)
            }
            .map { CommentDto.from(it) }
    }
}