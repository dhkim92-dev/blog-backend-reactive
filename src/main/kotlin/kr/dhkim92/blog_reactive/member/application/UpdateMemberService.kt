package kr.dhkim92.blog_reactive.member.application

import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.member.application.dto.UpdateMemberCommand
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.UpdateMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.LoadMemberPort
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.SaveMemberPort
import kr.dhkim92.blog_reactive.member.domain.exception.NotExistMemberException
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Mono

@Service
@Validated
class UpdateMemberService(
    val loadMemberPort: LoadMemberPort,
    val saveMemberPort: SaveMemberPort
): UpdateMemberUseCase {

    override fun execute(command: UpdateMemberCommand): Mono<Unit> {
        return loadMemberPort.findById(command.resourceId)
            .switchIfEmpty(Mono.error(NotExistMemberException()))
            .flatMap { member ->
                if ( command.memberId.value != member.identifier.value ) {
                    return@flatMap Mono.error<Unit>(ForbiddenException())
                }

                member.updateNickname(command.nickname)
                member.validate()
                saveMemberPort.save(member)
                    .thenReturn(Unit)
            }
    }
}