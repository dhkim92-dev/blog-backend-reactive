package kr.dhkim92.blog_reactive.member.adapter.out.persistence.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface MemberRepository : R2dbcRepository<MemberEntity, UUID> {

    fun findByIdAndIsDeletedFalse(id: UUID): Mono<MemberEntity>

    fun findAllByIdAndIsDeletedFalse(ids: Collection<UUID>): Flux<MemberEntity>

    fun findByAuthAccountIdAndIsDeletedFalse(authAccountId: UUID): Mono<MemberEntity>

    fun findByAuthAccountIdInAndIsDeletedFalse(authAccountIds: Collection<UUID>): Flux<MemberEntity>
}