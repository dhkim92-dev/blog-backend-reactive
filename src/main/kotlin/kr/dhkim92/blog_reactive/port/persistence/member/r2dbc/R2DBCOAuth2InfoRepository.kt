package kr.dhkim92.blog_reactive.port.persistence.member.r2dbc

import kr.dhkim92.blog_reactive.domain.member.r2dbc.OAuth2InfoEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface R2DBCOAuth2InfoRepository : R2dbcRepository<OAuth2InfoEntity, UUID> {

    fun findByUserId(userId: String): Mono<OAuth2InfoEntity>

    fun findByMemberId(memberId: UUID): Flux<OAuth2InfoEntity>
}