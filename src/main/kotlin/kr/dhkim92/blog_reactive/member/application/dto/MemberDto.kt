package kr.dhkim92.blog_reactive.member.application.dto

import kr.dhkim92.blog_reactive.member.domain.Member
import java.time.Instant
import java.util.UUID

class MemberDto(
    val memberId: UUID,
    val nickname: String,
    var createdAt: Instant
) {

    companion object {
        fun from(member: Member): MemberDto {
            return MemberDto(
                memberId=  member.identifier.value,
                nickname = member.nickname,
                createdAt = member.createdAt
            )
        }
    }
}