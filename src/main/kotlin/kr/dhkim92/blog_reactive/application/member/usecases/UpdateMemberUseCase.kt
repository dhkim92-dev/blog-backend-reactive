package kr.dhkim92.blog_reactive.application.member.usecases

import kr.dhkim92.blog_reactive.application.member.dto.UpdateMemberCommand
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface UpdateMemberUseCase {

    fun update(
        loginId: Id<Member, UUID>,
        memberId: Id<Member, UUID>,
        command: UpdateMemberCommand
    ): Mono<Void>
}