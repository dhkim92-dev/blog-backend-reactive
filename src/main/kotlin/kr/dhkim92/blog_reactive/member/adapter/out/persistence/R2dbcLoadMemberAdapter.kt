package kr.dhkim92.blog_reactive.member.adapter.out.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.member.adapter.out.persistence.r2dbc.MemberRepository
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.LoadMemberPort
import kr.dhkim92.blog_reactive.member.domain.Member
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class R2dbcLoadMemberAdapter(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper
): LoadMemberPort {

    override fun findById(memberId: Id<Member, UUID>): Mono<Member> {
        return memberRepository.findByIdAndIsDeletedFalse(memberId.value)
            .map(memberMapper::fromR2dbc)
    }

    override fun findAllById(memberIds: List<Id<Member, UUID>>): Flux<Member> {
        return memberRepository.findAllByIdAndIsDeletedFalse(memberIds.map{it.value})
            .map(memberMapper::fromR2dbc)
    }

    override fun findAllByAuthAccountIdIn(authAccountIds: List<Id<AuthInfo, UUID>>): Flux<Member> {
        return memberRepository.findByAuthAccountIdInAndIsDeletedFalse(authAccountIds.map{it.value})
            .map(memberMapper::fromR2dbc)
    }

    override fun findByAuthAccountId(authAccountId: Id<AuthInfo, UUID>): Mono<Member> {
        return memberRepository.findByAuthAccountIdAndIsDeletedFalse(authAccountId.value)
            .map(memberMapper::fromR2dbc)
    }
}