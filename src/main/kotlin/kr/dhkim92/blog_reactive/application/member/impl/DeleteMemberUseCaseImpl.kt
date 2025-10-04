package kr.dhkim92.blog_reactive.application.member.impl

import kr.dhkim92.blog_reactive.application.member.usecases.DeleteMemberUseCase
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional
class DeleteMemberUseCaseImpl(
    private val memberRepository: MemberRepository
): DeleteMemberUseCase {

    override fun delete(
        loginId: Id<Member, UUID>,
        memberId: Id<Member, UUID>
    ): Mono<Void> {
        val loginMemberMono = memberRepository.findById(loginId)
            .switchIfEmpty(Mono.error(UnauthorizedException("존재하지 않는 인증 회원")))
        val targetMemberMono = memberRepository.findById(memberId)
            .switchIfEmpty(Mono.error(NotFoundException("존재하지 않는 대상 회원")))

        return loginMemberMono.zipWith(targetMemberMono)
            .flatMap { tuple ->
                val loginMember = tuple.t1
                val targetMember = tuple.t2

                if (loginMember.id == targetMember.id || loginMember.isAdmin()) {
                    targetMember.markDeleted()
                    memberRepository.save(targetMember).then()
                } else {
                    Mono.error(ForbiddenException("권한이 없습니다."))
                }
            }
            .then()

    }
}