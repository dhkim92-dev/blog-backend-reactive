package kr.dhkim92.blog_reactive.member.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.member.application.dto.CreateMemberCommand
import kr.dhkim92.blog_reactive.member.application.dto.UpdateMemberCommand
import reactor.core.publisher.Mono

interface UpdateMemberUseCase {

    fun execute(command: UpdateMemberCommand): Mono<Unit>
}