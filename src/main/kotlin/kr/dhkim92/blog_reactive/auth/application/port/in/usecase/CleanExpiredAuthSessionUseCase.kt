package kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Mono
import java.util.UUID

interface CleanExpiredAuthSessionUseCase {

    fun execute(id: Id<AuthAccount, UUID>): Mono<Void>
}