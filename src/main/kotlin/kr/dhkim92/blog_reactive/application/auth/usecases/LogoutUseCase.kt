package kr.dhkim92.blog_reactive.application.auth.usecases

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface LogoutUseCase {

    fun logout(loginId: Id<Member, UUID>, refreshToken: String): Mono<Void>
}