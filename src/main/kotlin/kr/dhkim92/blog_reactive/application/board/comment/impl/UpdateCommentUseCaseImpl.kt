package kr.dhkim92.blog_reactive.application.board.comment.impl

import kr.dhkim92.blog_reactive.application.board.comment.dto.UpdateCommentCommand
import kr.dhkim92.blog_reactive.application.board.comment.usecases.UpdateCommentUseCase
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.board.ArticleRepository
import kr.dhkim92.blog_reactive.port.persistence.board.CommentRepository
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional
class UpdateCommentUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
): UpdateCommentUseCase {

    override fun update(
        loginId: Id<Member, UUID>,
        command: UpdateCommentCommand
    ): Mono<Void> {
        val memberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(Exception("권한 없음")))
        val commentMono = commentRepository.findById(command.commentId)
            .switchIfEmpty(Mono.error(Exception("댓글이 존재하지 않습니다.")))

        return Mono.zip(memberMono, commentMono)
            .flatMap { tuple ->
                val member = tuple.t1
                val comment = tuple.t2
                comment.updateContent(
                    requester = member,
                    newContent =command.content
                )
                commentRepository.save(comment)
            }
            .then()
    }
}