package kr.dhkim92.blog_reactive.application.board.comment.impl

import kr.dhkim92.blog_reactive.application.board.comment.usecases.DeleteCommentUseCase
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
class DeleteCommentUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
): DeleteCommentUseCase {


    override fun delete(loginId: Id<Member, UUID>, commentId: Id<Comment, UUID>): Mono<Void> {
        val memberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(Exception("권한 없음")))
        val commentMono = commentRepository.findById(commentId)
            .switchIfEmpty(Mono.error(Exception("댓글이 존재하지 않습니다.")))

        return Mono.zip(memberMono, commentMono)
            .flatMap { tuple ->
                val member = tuple.t1
                val comment = tuple.t2
                comment.markDeleted()
                // 부모 댓글 replyCount 감소
                val parentUpdateMono = comment.parentId?.let { parentId ->
                    commentRepository.findById(parentId)
                        .flatMap { parent ->
                            parent.decreaseReplyCount()
                            commentRepository.save(parent)
                        }
                } ?: Mono.empty()
                // Article의 commentCount 감소
                val articleUpdateMono = articleRepository.findById(comment.articleId)
                    .flatMap { article ->
                        article.decreaseCommentCount()
                        articleRepository.save(article)
                    }
                // 댓글 저장
                val commentSaveMono = commentRepository.save(comment)
                // 모든 작업이 끝난 후 완료
                Mono.`when`(parentUpdateMono, articleUpdateMono, commentSaveMono)
            }
            .then()
    }
}