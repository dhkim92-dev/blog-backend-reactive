package kr.dhkim92.blog_reactive.application.member.dto

data class CreateMemberCommand(
    val email: String,
    val nickname: String,
    val password: String
) {

}