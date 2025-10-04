package kr.dhkim92.blog_reactive.application.member.usecases

import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface QueryMemberUseCase {

    fun getMember(memberId: Id<Member, UUID>): Mono<MemberDto>

    fun getMemberByOAuth2UserInfo(userInfo: OAuth2UserInfo): Mono<MemberDto>
}