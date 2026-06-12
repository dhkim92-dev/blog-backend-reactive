package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface LoadAuthSessionPort {

    fun findByAuthAccountId(id: Id<AuthAccount, UUID>): Flux<AuthSession>

    fun findById(id: Id<AuthSession, UUID>): Mono<AuthSession>
}