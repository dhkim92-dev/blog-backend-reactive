package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import reactor.core.publisher.Mono

interface SaveEmailPasswordCredentialPort {

    fun save(credential: EmailPasswordCredential): Mono<EmailPasswordCredential>
}