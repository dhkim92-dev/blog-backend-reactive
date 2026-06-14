package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import reactor.core.publisher.Mono

interface SaveAuthAccountPort {

    fun save(account: AuthAccount): Mono<AuthAccount>
}