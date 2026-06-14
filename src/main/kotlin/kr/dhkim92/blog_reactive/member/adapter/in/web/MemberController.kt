package kr.dhkim92.blog_reactive.member.adapter.`in`.web

import jakarta.validation.Valid
import kr.dhkim92.blog_reactive.common.annotations.AuthId
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.member.application.dto.CreateMemberRequest
import kr.dhkim92.blog_reactive.member.application.dto.MemberResponse
import kr.dhkim92.blog_reactive.member.application.dto.QueryMemberRequest
import kr.dhkim92.blog_reactive.member.application.dto.UpdateMemberRequest
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.CreateMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.DeleteMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.GetMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.`in`.usecase.UpdateMemberUseCase
import kr.dhkim92.blog_reactive.member.application.port.`in`.web.MemberApi
import kr.dhkim92.blog_reactive.member.domain.Member
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@Validated
class MemberController(
    private val createMemberUseCase: CreateMemberUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val getMemberUseCase: GetMemberUseCase,
): MemberApi {


    @Envelope(status = HttpStatus.CREATED, message = "Member created successfully", code = "M001")
    override fun postMember(
        @AuthId authId: Id<AuthInfo, UUID>,
        @RequestBody @Valid request: CreateMemberRequest
    ): Mono<Void> {
        return createMemberUseCase.execute(request.toCommand(authId))
            .then()
    }

    @Envelope(status = HttpStatus.OK, message = "Member updated successfully", code = "M002")
    override fun putMember(
        @Login loginId: Id<LoginMember, UUID>,
        @PathVariable memberId: UUID,
        @RequestBody @Valid request: UpdateMemberRequest
    ): Mono<Void> {
        return updateMemberUseCase.execute(request.toCommand(loginId, memberId))
            .then()
    }

    @Envelope(status = HttpStatus.NO_CONTENT, message = "Member deleted successfully", code = "M003")
    override fun deleteMember(
        @Login loginId: Id<LoginMember, UUID>,
        @PathVariable memberId: UUID
    ): Mono<Void> {
        return deleteMemberUseCase.execute(loginId, Id.of(Member::class, memberId))
    }

    @Envelope(status = HttpStatus.OK, message = "Member retrieved successfully", code = "M004")
    override fun getMember(
        @PathVariable memberId: UUID
    ): Mono<MemberResponse> {
        return getMemberUseCase.execute(Id.of(Member::class, memberId))
            .map(MemberResponse::from)
    }

    @Envelope(status = HttpStatus.OK, message = "Members retrieved successfully", code = "M005")
    override fun getMembers(@RequestBody @Valid request: QueryMemberRequest): Mono<ListResponse<MemberResponse>> {
        return Mono.defer {
            val memberIds = request.toMemberIds()

            getMemberUseCase.execute(memberIds)
                .collectList()
                .map { members ->
                    val memberMap = members.associateBy { it.memberId }
                    val payload = memberIds.map { memberId ->
                        val member = memberMap[memberId.value]

                        if ( member == null ) {
                            MemberResponse.deletedMember(memberId)
                        } else {
                            MemberResponse.from(member)
                        }
                    }
                    ListResponse.from(payload)
                }
        }
    }
}