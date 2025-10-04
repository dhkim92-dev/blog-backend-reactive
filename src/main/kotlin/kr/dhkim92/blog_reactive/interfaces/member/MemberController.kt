package kr.dhkim92.blog_reactive.interfaces.member

import jakarta.validation.Valid
import kr.dhkim92.blog_reactive.application.member.MemberServiceFacade
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.member.dto.CreateMemberRequest
import kr.dhkim92.blog_reactive.interfaces.member.dto.MemberResponse
import kr.dhkim92.blog_reactive.interfaces.member.dto.UpdateMemberRequest
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class MemberController(
    private val memberServiceFacade: MemberServiceFacade
): MemberApi {

    @Envelope(status = CREATED, message = "Member created successfully", code = "M001")
    override fun postMember(
        @RequestBody @Valid request: CreateMemberRequest
    ): Mono<MemberResponse> {
        return memberServiceFacade.createMember(request.toCommand())
            .map { MemberResponse.from(it) }
    }

    @Envelope(status = OK, message = "Member updated successfully", code = "M002")
    override fun putMember(
        @Login loginId: Id<Member, UUID>,
        @PathVariable memberId: UUID,
        @RequestBody @Valid request: UpdateMemberRequest
    ): Mono<Void> {
        return memberServiceFacade.updateMember(
            loginId,
            Id.of(Member::class, memberId),
            request.toCommand()
        )
    }

    @Envelope(status = NO_CONTENT, message = "Member deleted successfully", code = "M003")
    override fun deleteMember(
        @Login loginId: Id<Member, UUID>,
        @PathVariable memberId: UUID
    ): Mono<Void> {
        return memberServiceFacade.deleteMember(
            loginId,
            Id.of(Member::class, memberId)
        )
    }

    @Envelope(status = OK, message = "Member retrieved successfully", code = "M004")
    override fun getMember(
        @PathVariable memberId: UUID
    ): Mono<MemberResponse> {
        return memberServiceFacade.getMember(
            Id.of(Member::class, memberId)
        ).map { MemberResponse.from(it) }
    }
}