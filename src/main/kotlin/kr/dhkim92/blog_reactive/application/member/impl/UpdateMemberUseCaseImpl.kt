package kr.dhkim92.blog_reactive.application.member.impl

import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import kr.dhkim92.blog_reactive.application.member.dto.UpdateMemberCommand
import kr.dhkim92.blog_reactive.application.member.usecases.UpdateMemberUseCase
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional
class UpdateMemberUseCaseImpl(
    private val memberRepository: MemberRepository
): UpdateMemberUseCase {

    override fun update(loginId: Id<Member, UUID>, memberId: Id<Member, UUID>, command: UpdateMemberCommand): Mono<Void> {
        val requesterMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(kr.dhkim92.blog_reactive.common.error.UnauthorizedException("존재하지 않는 인증 회원")))
        val targetMono = memberRepository.findById(memberId)
            .switchIfEmpty(Mono.error(NotFoundException()))

        return requesterMono.zipWith(targetMono)
            .flatMap { tuple ->
                val requester = tuple.t1
                val target = tuple.t2
                target.changeNickname(requester, command.nickname)
                memberRepository.save(target)
            }
            .then()
    }
}