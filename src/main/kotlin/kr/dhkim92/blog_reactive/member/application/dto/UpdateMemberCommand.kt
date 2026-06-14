package kr.dhkim92.blog_reactive.member.application.dto

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import kr.dhkim92.blog_reactive.member.domain.Member
import org.hibernate.validator.constraints.Length
import java.util.UUID

data class UpdateMemberCommand(
    val memberId: Id<LoginMember, UUID>,
    val resourceId: Id<Member, UUID>,
    @field: Length(min = 3, max = 20, message = "Nickname must be between 3 and 20 characters")
    val nickname: String
) {
}