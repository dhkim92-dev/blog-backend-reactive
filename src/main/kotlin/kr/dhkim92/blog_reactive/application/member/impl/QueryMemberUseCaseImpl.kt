package kr.dhkim92.blog_reactive.application.member.impl

import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import kr.dhkim92.blog_reactive.application.member.usecases.QueryMemberUseCase
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional(readOnly = true)
class QueryMemberUseCaseImpl(
    private val memberRepository: MemberRepository
): QueryMemberUseCase {

    override fun getMember(memberId: Id<Member, UUID>): Mono<MemberDto> {
        return memberRepository.findById(memberId)
            .switchIfEmpty(Mono.error(NotFoundException("존재하지 않는 회원")))
            .map { MemberDto.from(it) }
    }

    override fun getMemberByOAuth2UserInfo(userInfo: OAuth2UserInfo): Mono<MemberDto> {
        return memberRepository.findByOAuth2UserId("${userInfo.getProvider().name.lowercase()}:${userInfo.getId()}")
            .switchIfEmpty(Mono.error(NotFoundException("존재하지 않는 회원")))
            .map { MemberDto.from(it) }
    }
}