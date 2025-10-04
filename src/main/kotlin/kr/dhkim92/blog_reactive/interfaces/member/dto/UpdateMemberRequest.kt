package kr.dhkim92.blog_reactive.interfaces.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.application.member.dto.UpdateMemberCommand

@Schema(description = "회원 수정 요청")
data class UpdateMemberRequest(
    @field:Schema(description = "닉네임", example = "new_nickname", required = true)
    @field: NotEmpty(message = "닉네임은 필수 입니다. 수정하지 않더라도 기존의 값을 입력해야 합니다.")
    val nickname: String
) {

    fun toCommand(): UpdateMemberCommand {
        return UpdateMemberCommand(
            nickname = this.nickname
        )
    }
}