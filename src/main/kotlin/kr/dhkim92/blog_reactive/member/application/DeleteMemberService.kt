package kr.dhkim92.blog_reactive.member.application

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.DeleteMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.LoadMemberPort
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.SaveMemberPort
import kr.dhkim92.blog_reactive.member.domain.Member
import kr.dhkim92.blog_reactive.member.domain.exception.NotExistMemberException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.UUID

@Service
class DeleteMemberService(
    private val loadMemberPort: LoadMemberPort,
    private val saveMemberPort: SaveMemberPort
): DeleteMemberUseCase {

    override fun execute(loginId: Id<LoginMember, UUID>, resourceId: Id<Member, UUID>): Mono<Void> {
        return loadMemberPort.findById(resourceId)
            .switchIfEmpty { Mono.error(NotExistMemberException()) }
            .flatMap { member->
                if ( member.identifier.value != loginId.value ) {
                    return@flatMap Mono.error<Void>(ForbiddenException())
                }
                if ( !member.isDeleted ) {
                    member.markDeleted()
                    saveMemberPort.save(member)
                }
                Mono.empty()
            }
    }
}