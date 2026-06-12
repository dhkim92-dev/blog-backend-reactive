package kr.dhkim92.blog_reactive.auth.application.dto

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.auth.domain.AuthRole

class JwtClaims(
    val sub: String,
    val nickname: String="",
    val role: AuthRole = AuthRole.MEMBER
){

    companion object {
        fun of( account: AuthAccount, member: AuthMember) = JwtClaims(
                sub = member.identifier.value.toString(),
                nickname = member.nickname,
                role = account.role
            )
    }
}