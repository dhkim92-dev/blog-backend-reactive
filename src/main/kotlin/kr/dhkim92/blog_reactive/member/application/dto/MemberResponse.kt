package kr.dhkim92.blog_reactive.member.application.dto

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.response.BaseResponse
import kr.dhkim92.blog_reactive.member.domain.Member
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

class MemberResponse(
    val id: UUID,
    val nickname: String,
    val createdAt: Instant,
    val isDeleted: Boolean = false
): BaseResponse() {

    companion object {

        fun from(dto: Mono<MemberDto>): Mono<MemberResponse> {
            return dto.map {
                MemberResponse(
                    id = it.memberId,
                    nickname = it.nickname,
                    createdAt = it.createdAt,
                )
            }
        }

        fun from(dto: MemberDto): MemberResponse {
            return MemberResponse(
                id = dto.memberId,
                nickname = dto.nickname,
                createdAt = dto.createdAt,
            )
        }

        fun deletedMember(id: Id<Member, UUID>) = MemberResponse(
            id = id.value,
            nickname = "탈퇴한 멤버",
            createdAt = Instant.now(),
            isDeleted = true
        )
    }
}