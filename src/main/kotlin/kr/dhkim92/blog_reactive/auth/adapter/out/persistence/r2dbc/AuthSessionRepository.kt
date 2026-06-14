package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface AuthSessionRepository : R2dbcRepository<AuthSessionEntity, UUID> {

    fun findByAuthAccountId(authAccountId: UUID): Flux<AuthSessionEntity>
}