package kr.dhkim92.blog_reactive.member.application.port.`in`.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.member.application.dto.CreateMemberRequest
import kr.dhkim92.blog_reactive.member.application.dto.MemberResponse
import kr.dhkim92.blog_reactive.member.application.dto.QueryMemberRequest
import kr.dhkim92.blog_reactive.member.application.dto.UpdateMemberRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name = "Member API", description = "Member CRUD API endpoints")
@RequestMapping("/api/v1/members")
interface MemberApi {

    @Operation(
        summary = "Create Member",
        description = "Creates a new member with the provided details."
    )
    @ApiResponse(responseCode = "201", description = "Member created successfully")
    @PostMapping("")
    fun postMember(
        authId: Id<AuthInfo, UUID>,
        request: CreateMemberRequest
    ): Mono<Void>

    @Operation(
        summary = "Update Member",
        description = "Updates the details of an existing member identified by memberId."
    )
    @ApiResponse(responseCode = "200", description = "Member updated successfully")
    @PutMapping("/{memberId}")
    fun putMember(
        loginId: Id<LoginMember, UUID>,
        memberId: UUID,
        request: UpdateMemberRequest
    ): Mono<Void>

    @Operation(
        summary = "Delete Member",
        description = "Deletes the member identified by memberId."
    )
    @ApiResponse(responseCode = "204", description = "Member deleted successfully")
    @DeleteMapping("/{memberId}")
    fun deleteMember(
        loginId: Id<LoginMember, UUID>,
        memberId: UUID
    ): Mono<Void>

    @Operation(
        summary = "Get Member",
        description = "Retrieves the details of a member identified by memberId."
    )
    @ApiResponse(responseCode = "200", description = "Member retrieved successfully")
    @GetMapping("/{memberId}")
    fun getMember(memberId: UUID): Mono<MemberResponse>

    @Operation(
        summary = "Get members",
        description = "Retrieves a list of members."
    )
    @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    @PostMapping
    fun getMembers(request: QueryMemberRequest): Mono<ListResponse<MemberResponse>>
}