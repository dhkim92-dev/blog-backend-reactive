package kr.dhkim92.blog_reactive.interfaces.member.dto

import kr.dhkim92.blog_reactive.application.member.dto.CreateMemberCommand

class CreateMemberRequest(
    val email: String,
    val nickname: String,
    val password: String
) {

    fun toCommand(): CreateMemberCommand {
        return CreateMemberCommand(
            email = email,
            nickname = nickname,
            password = password
        )
    }
}