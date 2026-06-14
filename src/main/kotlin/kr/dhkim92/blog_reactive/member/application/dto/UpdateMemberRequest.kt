package kr.dhkim92.blog_reactive.member.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import kr.dhkim92.blog_reactive.member.domain.Member
import org.hibernate.validator.constraints.Length
import java.util.UUID

@Schema(description = "회원 수정 요청")
data class UpdateMemberRequest(
    @field:Schema(description = "닉네임", example = "new_nickname", required = true)
    @field: NotEmpty(message = "닉네임은 필수 입니다. 수정하지 않더라도 기존의 값을 입력해야 합니다.")
    @field: Length(min=3, max=20, message = "닉네임은 3~20자 사이로 입력해야 합니다.")
    val nickname: String
) {

    fun toCommand(loginId: Id<LoginMember, UUID>, resourceId: UUID): UpdateMemberCommand {
        return UpdateMemberCommand(
            memberId = loginId,
            resourceId = Id.of(Member::class, resourceId),
            nickname = this.nickname
        )
    }
}