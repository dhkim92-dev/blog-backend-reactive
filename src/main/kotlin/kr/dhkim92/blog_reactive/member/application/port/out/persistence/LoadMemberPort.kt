package kr.dhkim92.blog_reactive.member.application.port.out.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.member.domain.Member
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface LoadMemberPort {

    fun findById(memberId: Id<Member, UUID>): Mono<Member>

    fun findAllById(memberIds: List<Id<Member, UUID>>): Flux<Member>

    fun findByAuthAccountId(authAccountId: Id<AuthInfo, UUID>): Mono<Member>

    fun findAllByAuthAccountIdIn(authAccountIds: List<Id<AuthInfo, UUID>>): Flux<Member>
}