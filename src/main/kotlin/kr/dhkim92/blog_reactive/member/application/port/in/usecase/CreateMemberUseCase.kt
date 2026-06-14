package kr.dhkim92.blog_reactive.member.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.member.application.dto.CreateMemberCommand
import reactor.core.publisher.Mono

interface CreateMemberUseCase {

    fun execute(command: CreateMemberCommand): Mono<Unit>
}