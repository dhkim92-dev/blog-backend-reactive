package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import kr.dhkim92.blog_reactive.common.data.Email
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Mono
import java.util.UUID

interface LoadEmailPasswordCredentialPort {

    fun findByEmail(email: Email): Mono<EmailPasswordCredential>

    fun findByAuthAccountId(id: Id<AuthAccount, UUID>): Mono<EmailPasswordCredential>
}