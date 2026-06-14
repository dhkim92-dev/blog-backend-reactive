package kr.dhkim92.blog_reactive.member.application

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.member.application.dto.CreateMemberCommand
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.CreateMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.LoadMemberPort
import kr.dhkim92.blog_reactive.member.application.port.out.persistence.SaveMemberPort
import kr.dhkim92.blog_reactive.member.domain.Member
import kr.dhkim92.blog_reactive.member.domain.exception.AlreadyExistMemberException
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Mono

@Service
@Validated
class CreateMemberService(
    private val loadMemberPort: LoadMemberPort,
    private val saveMemberPort: SaveMemberPort
): CreateMemberUseCase {

    override fun execute(command: CreateMemberCommand): Mono<Unit> {
        // 1. AuthInfoId 로 기존에 생성된 회원 엔티티가 있는지 확인
        //     a. 존재하면 AlreadyExistMemberException
        //     b. 존재 하지 않으면 2로 진행
        // 2. Command 객체를 이용하여 Member 엔티티를 생성
        // 3. MemberEntity validation
        // 4. save

        return loadMemberPort.findByAuthAccountId(command.authAccountId)
            .flatMap { Mono.error<Unit>(AlreadyExistMemberException()) }
            .switchIfEmpty(Mono.defer {
                val member = Member(
                    authAccountId = command.authAccountId,
                    nickname = command.nickname
                )
                member.validate()
                saveMemberPort.save(member)
                    .thenReturn(Unit)
            })
    }
}