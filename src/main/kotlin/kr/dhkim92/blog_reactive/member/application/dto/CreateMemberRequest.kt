package kr.dhkim92.blog_reactive.member.application.dto

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import java.util.UUID


class CreateMemberRequest(
    val nickname: String,
) {

    fun toCommand(authId: Id<AuthInfo, UUID>): CreateMemberCommand {
        return CreateMemberCommand(
            authAccountId = authId,
            nickname = nickname,
        )
    }
}