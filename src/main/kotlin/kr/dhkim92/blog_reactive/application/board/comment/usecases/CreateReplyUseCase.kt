package kr.dhkim92.blog_reactive.application.board.comment.usecases

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentDto
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateReplyCommand
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface CreateReplyUseCase {

    fun createReply(
        loginId: Id<Member, UUID>,
        command: CreateReplyCommand
    ): Mono<CommentDto>
}