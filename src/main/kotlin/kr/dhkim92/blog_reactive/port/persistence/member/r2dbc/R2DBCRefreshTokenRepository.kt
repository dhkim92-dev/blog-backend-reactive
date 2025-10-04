package kr.dhkim92.blog_reactive.port.persistence.member.r2dbc

import kr.dhkim92.blog_reactive.domain.member.RefreshToken
import kr.dhkim92.blog_reactive.domain.member.r2dbc.RefreshTokenEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface R2DBCRefreshTokenRepository : R2dbcRepository<RefreshTokenEntity, UUID> {

    fun findByMemberId(memberId: UUID): Flux<RefreshTokenEntity>

    fun findByToken(token: String): Mono<RefreshTokenEntity>
}