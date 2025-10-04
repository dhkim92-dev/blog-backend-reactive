package kr.dhkim92.blog_reactive.application.member.impl

import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.member.dto.CreateMemberCommand
import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import kr.dhkim92.blog_reactive.application.member.usecases.CreateMemberUseCase
import kr.dhkim92.blog_reactive.common.error.ConflictException
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.MemberRole
import kr.dhkim92.blog_reactive.domain.member.OAuth2Info
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class CreateMemberUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
): CreateMemberUseCase {

    @Transactional
    override fun create(command: CreateMemberCommand): Mono<MemberDto> {
        return memberRepository.existsByEmail(command.email)
            .flatMap { exists ->
                if (exists) {
                    return@flatMap Mono.error<Member>(ConflictException(message="이미 등록된 이메일입니다."))
                }
                val encodedPassword = passwordEncoder.encode(command.password)
                val member = Member(
                    nickname = command.nickname,
                    email = command.email,
                    password = encodedPassword,
                    role = MemberRole.MEMBER
                )
                memberRepository.save(member)
            }
            .map { MemberDto.from(it) }
    }

    @Transactional
    override fun createByOAuth2UserInfo(userInfo: OAuth2UserInfo): Mono<MemberDto> {
        return memberRepository.findByOAuth2UserId(userId = userInfo.getUserId())
            .flatMap {
                Mono.error<Member>(ConflictException(message="이미 등록된 회원입니다."))
            }
            .switchIfEmpty(Mono.defer {
                memberRepository.save(Member(
                    nickname = userInfo.getNickName(),
                    email = "${userInfo.getUserId()}@dohoon-kim.kr",
                    password = passwordEncoder.encode(UUID.randomUUID().toString()),
                    role = MemberRole.MEMBER)
                ).flatMap {
                    linkOAuth2InfoToMember(it, userInfo)
                    memberRepository.save(it)
                }
            })
            .map { MemberDto.from(it) }
    }

private fun linkOAuth2InfoToMember(member: Member, userInfo: OAuth2UserInfo) {
        member.linkOAuth2Info(
            member,
            OAuth2Info(
                memberId = member.id!!,
                provider = userInfo.getProvider(),
                userId = userInfo.getUserId(),
            )
        )
    }
}