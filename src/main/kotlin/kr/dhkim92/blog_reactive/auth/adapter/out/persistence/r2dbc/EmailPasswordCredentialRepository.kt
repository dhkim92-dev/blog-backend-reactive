package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface EmailPasswordCredentialRepository: R2dbcRepository<EmailPasswordCredentialEntity, UUID> {

    fun findByAuthAccountId(authAccountId: UUID): Mono<EmailPasswordCredentialEntity>

    fun findByEmail(email: String): Mono<EmailPasswordCredentialEntity>
}