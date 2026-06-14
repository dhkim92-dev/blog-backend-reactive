package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.EmailPasswordCredentialRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveEmailPasswordCredentialPort
import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class R2dbcSaveEmailPasswordCredentialAdapter(
    private val emailPasswordCredentialRepository: EmailPasswordCredentialRepository,
    private val emailPasswordCredentialMapper: EmailPasswordCredentialMapper
): SaveEmailPasswordCredentialPort {

    override fun save(credential: EmailPasswordCredential): Mono<EmailPasswordCredential> {
        return emailPasswordCredentialRepository.save(emailPasswordCredentialMapper.toR2dbc(credential))
            .map(emailPasswordCredentialMapper::fromR2dbc)
    }
}