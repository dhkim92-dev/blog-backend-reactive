package kr.dhkim92.blog_reactive.interfaces.member

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.member.dto.CreateMemberRequest
import kr.dhkim92.blog_reactive.interfaces.member.dto.MemberResponse
import kr.dhkim92.blog_reactive.interfaces.member.dto.UpdateMemberRequest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
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
    fun postMember(request: CreateMemberRequest): Mono<MemberResponse>

    @Operation(
        summary = "Update Member",
        description = "Updates the details of an existing member identified by memberId."
    )
    @ApiResponse(responseCode = "200", description = "Member updated successfully")
    @PutMapping("/{memberId}")
    fun putMember(
        loginId: Id<Member, UUID>,
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
        loginId: Id<Member, UUID>,
        memberId: UUID
    ): Mono<Void>

    @Operation(
        summary = "Get Member",
        description = "Retrieves the details of a member identified by memberId."
    )
    @ApiResponse(responseCode = "200", description = "Member retrieved successfully")
    @GetMapping("/{memberId}")
    fun getMember(memberId: UUID): Mono<MemberResponse>
}