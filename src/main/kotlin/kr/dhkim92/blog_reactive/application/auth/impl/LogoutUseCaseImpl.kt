package kr.dhkim92.blog_reactive.application.auth.impl

import kr.dhkim92.blog_reactive.application.auth.usecases.LogoutUseCase
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional
class LogoutUseCaseImpl(
    private val memberRepository: MemberRepository,
) : LogoutUseCase {

    override fun logout(loginId: Id<Member, UUID>, refreshToken: String): Mono<Void> {
        return memberRepository.findById(loginId)
            .flatMap { member ->
                val existToken = member.refreshTokens.find { it.token == refreshToken && !it.isDeleted }
                if (existToken != null) {
                    member.revokeRefreshToken(member, existToken)
                    memberRepository.save(member).then()
                } else {
                    Mono.empty()
                }
            }
            .then()
    }
}