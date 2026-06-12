package kr.dhkim92.blog_reactive.application.board.comment.usecases

import kr.dhkim92.blog_reactive.application.board.comment.dto.CommentDto
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateCommentCommand
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface CreateCommentUseCase {

    fun create(loginId: Id<Member, UUID>,
               command: CreateCommentCommand): Mono<CommentDto>
}