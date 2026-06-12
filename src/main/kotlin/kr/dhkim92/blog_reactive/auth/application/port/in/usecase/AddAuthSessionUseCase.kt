package kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.auth.domain.RefreshToken
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Mono
import java.util.UUID

interface AddAuthSessionUseCase {

    fun execute(id: Id<AuthAccount, UUID>, session: AuthSession): Mono<AuthSession>
}