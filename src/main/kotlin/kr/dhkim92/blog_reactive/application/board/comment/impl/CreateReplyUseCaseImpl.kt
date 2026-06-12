package kr.dhkim92.blog_reactive.application.board.comment.impl

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentDto
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateReplyCommand
import kr.dhkim92.blog_reactive.application.board.comment.usecases.CreateReplyUseCase
import kr.dhkim92.blog_reactive.common.error.BadRequestException
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Comment
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
class CreateReplyUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository
): CreateReplyUseCase {

    override fun createReply(
        loginId: Id<Member, UUID>,
        command: CreateReplyCommand
    ): Mono<CommentDto> {
        val memberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(ForbiddenException(message = "권한 없음")))
        val parentCommentMono = commentRepository.findById(command.parentId)
            .switchIfEmpty(Mono.error(BadRequestException(message = "삭제된 댓글입니다.")))

        return memberMono.zipWith(parentCommentMono)
            .flatMap { tuple ->
                val member = tuple.t1
                val parentComment = tuple.t2
                // parentComment에서 articleId 추출 후 article 조회
                articleRepository.findById(parentComment.articleId)
                    .flatMap { article ->
                        // Comment 생성
                        val comment = Comment.create(
                            writer = member,
                            article = article,
                            parent = parentComment,
                            content = command.content
                        )
                        // 저장
                        commentRepository.save(comment)
                    }
            }
            .map { savedComment -> CommentDto.from(savedComment) }
    }
}