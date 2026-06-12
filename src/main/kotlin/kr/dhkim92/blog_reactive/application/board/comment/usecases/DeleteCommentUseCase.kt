package kr.dhkim92.blog_reactive.application.board.comment.usecases

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface DeleteCommentUseCase {

    fun delete(
        loginId: Id<Member, UUID>,
        commentId: Id<Comment, UUID>
    ): Mono<Void>
}