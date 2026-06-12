package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import reactor.core.publisher.Mono

interface SaveAuthSessionPort {

    fun save(session: AuthSession): Mono<AuthSession>

    fun delete(session: AuthSession): Mono<Unit>
}