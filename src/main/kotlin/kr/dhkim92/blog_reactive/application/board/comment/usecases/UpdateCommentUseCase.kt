package kr.dhkim92.blog_reactive.application.board.comment.usecases

import kr.dhkim92.blog_reactive.application.board.comment.dto.UpdateCommentCommand
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface UpdateCommentUseCase {

    fun update(
        loginId: Id<Member, UUID>,
        command: UpdateCommentCommand
    ): Mono<Void>
}