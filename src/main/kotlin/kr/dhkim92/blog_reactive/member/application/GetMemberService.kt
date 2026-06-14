package kr.dhkim92.blog_reactive.member.application

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.member.application.dto.MemberDto
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.GetMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.LoadMemberPort
import kr.dhkim92.blog_reactive.member.domain.Member
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.util.UUID

@Service
@Validated
class GetMemberService(
     private val loadMemberPort: LoadMemberPort,
): GetMemberUseCase {

    override fun execute(authAccountId: Id<AuthInfo, UUID>): Mono<MemberDto> {
        return loadMemberPort.findByAuthAccountId(authAccountId)
            .switchIfEmpty { Mono.error(NotFoundException()) }
            .map(MemberDto::from)
    }

    override fun execute(authAccountIds: List<Id<AuthInfo, UUID>>): Flux<MemberDto> {
        return loadMemberPort.findAllByAuthAccountIdIn(authAccountIds)
            .map(MemberDto::from)
    }

    override fun execute(memberId: Id<Member, UUID>): Mono<MemberDto> {
        return loadMemberPort.findById(memberId)
            .switchIfEmpty { Mono.error(NotFoundException()) }
            .map(MemberDto::from)
    }

    override fun execute(memberIds: List<Id<Member, UUID>>): Flux<MemberDto> {
        return loadMemberPort.findAllById(memberIds)
            .map(MemberDto::from)
    }
}