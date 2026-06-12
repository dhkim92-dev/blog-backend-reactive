package kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface LogoutUseCase {

    fun execute(loginId: Id<Member, UUID>, refreshToken: String): Mono<Void>
}