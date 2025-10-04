package kr.dhkim92.blog_reactive.application.board.post.usecases

import kr.dhkim92.blog_reactive.application.board.post.dto.UpdatePostCommand
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface UpdatePostUseCase {

    fun update(loginId: Id<Member, UUID>, command: UpdatePostCommand): Mono<Void>
}