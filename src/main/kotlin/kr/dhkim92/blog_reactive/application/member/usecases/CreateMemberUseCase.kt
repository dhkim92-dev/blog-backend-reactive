package kr.dhkim92.blog_reactive.application.member.usecases

import kr.dhkim92.blog_reactive.auth.application.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.member.dto.CreateMemberCommand
import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import reactor.core.publisher.Mono

interface CreateMemberUseCase {

    fun create(command: CreateMemberCommand): Mono<MemberDto>

    fun createByOAuth2UserInfo(userInfo: OAuth2UserInfo): Mono<MemberDto>
}