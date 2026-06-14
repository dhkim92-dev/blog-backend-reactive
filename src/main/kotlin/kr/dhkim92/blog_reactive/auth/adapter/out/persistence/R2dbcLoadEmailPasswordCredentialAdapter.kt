package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.EmailPasswordCredentialRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadEmailPasswordCredentialPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import kr.dhkim92.blog_reactive.common.data.Email
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class R2dbcLoadEmailPasswordCredentialAdapter(
    private val emailPasswordCredentialRepository: EmailPasswordCredentialRepository,
    private val emailPasswordCredentialMapper: EmailPasswordCredentialMapper
): LoadEmailPasswordCredentialPort {

    override fun findByEmail(email: Email): Mono<EmailPasswordCredential> {
        return emailPasswordCredentialRepository.findByEmail(email.value)
            .map(emailPasswordCredentialMapper::fromR2dbc)
    }

    override fun findByAuthAccountId(id: Id<AuthAccount, UUID>): Mono<EmailPasswordCredential> {
        return emailPasswordCredentialRepository.findByAuthAccountId(id.value)
            .map(emailPasswordCredentialMapper::fromR2dbc)
    }
}