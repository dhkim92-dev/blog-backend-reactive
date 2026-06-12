package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import kr.dhkim92.blog_reactive.common.data.Email
import reactor.core.publisher.Mono

interface LoadEmailPasswordCredentialPort {

    fun findByEmail(email: Email): Mono<EmailPasswordCredential>
}