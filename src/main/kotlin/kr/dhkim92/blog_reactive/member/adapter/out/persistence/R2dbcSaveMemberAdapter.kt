package kr.dhkim92.blog_reactive.member.adapter.out.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.member.adapter.out.persistence.r2dbc.MemberRepository
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.SaveMemberPort
import kr.dhkim92.blog_reactive.member.domain.Member
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class R2dbcSaveMemberAdapter(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper
) : SaveMemberPort {

    override fun save(member: Member): Mono<Member> {
        return memberRepository.save(memberMapper.toR2dbc(member))
            .map(memberMapper::fromR2dbc)
    }

    override fun deleteById(id: Id<Member, UUID>): Mono<Void> {
        return memberRepository.deleteById(id.value)
    }
}