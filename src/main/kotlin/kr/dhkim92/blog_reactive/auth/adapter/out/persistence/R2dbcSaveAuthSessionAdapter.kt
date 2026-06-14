package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthSessionRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveAuthSessionPort
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class R2dbcSaveAuthSessionAdapter(
    private val authSessionRepository: AuthSessionRepository,
    private val authSessionMapper: AuthSessionMapper
): SaveAuthSessionPort {

    override fun save(session: AuthSession): Mono<AuthSession> {
        return authSessionRepository.save(authSessionMapper.toR2dbc(session))
            .map(authSessionMapper::fromR2dbc)
    }

    override fun delete(session: AuthSession): Mono<Void> {
        if ( session.id != null) {
            return authSessionRepository.deleteById(session.identifier.value).then()
        }
        return Mono.empty()
    }
}