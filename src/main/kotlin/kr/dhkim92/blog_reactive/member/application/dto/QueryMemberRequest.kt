package kr.dhkim92.blog_reactive.member.application.dto

import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.member.domain.Member
import java.util.UUID

class QueryMemberRequest(
    @field: NotEmpty(message="MemberIds cannot be empty")
    val memberIds: List<UUID>
) {

    fun toMemberIds(): List<Id<Member, UUID>> {
        return memberIds.map { Id(Member::class,it) }
    }
}