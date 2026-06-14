package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthSessionRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthSessionPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class R2dbcLoadAuthSessionAdapter(
    private val authSessionRepository: AuthSessionRepository,
    private val authSessionMapper: AuthSessionMapper
): LoadAuthSessionPort {

    override fun findById(id: Id<AuthSession, UUID>): Mono<AuthSession> {
        return authSessionRepository.findById(id.value)
            .map(authSessionMapper::fromR2dbc)
    }

    override fun findByAuthAccountId(id: Id<AuthAccount, UUID>): Flux<AuthSession> {
        return authSessionRepository.findByAuthAccountId(id.value)
            .map { session -> authSessionMapper.fromR2dbc(session) }
    }
}
