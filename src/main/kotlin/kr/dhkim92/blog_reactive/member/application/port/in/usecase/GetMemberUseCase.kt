package kr.dhkim92.blog_reactive.member.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.member.application.dto.MemberDto
import kr.dhkim92.blog_reactive.member.domain.Member
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface GetMemberUseCase {

    fun execute(authAccountIds: List<Id<AuthInfo, UUID>>): Flux<MemberDto>

    fun execute(authAccountId: Id<AuthInfo, UUID>): Mono<MemberDto>

    fun execute(memberIds: List<Id<Member, UUID>>): Flux<MemberDto>

    fun execute(memberId: Id<Member, UUID>): Mono<MemberDto>
}