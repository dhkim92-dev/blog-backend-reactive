package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface AuthMemberRepository : R2dbcRepository<AuthMemberRepository, UUID> {

    fun findByAuthAccountId(authAccountId: UUID): Mono<AuthMember>
}