package kr.dhkim92.blog_reactive.application.board.post.usecases

import kr.dhkim92.blog_reactive.application.board.post.dto.CreatePostCommand
import kr.dhkim92.blog_reactive.application.board.post.dto.PostCommandDto
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface CreatePostUseCase {

    fun create(
        loginId: Id<Member, UUID>,
        command: CreatePostCommand
    ): Mono<PostCommandDto>
}