package kr.dhkim92.blog_reactive.application.board.dto

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import java.util.UUID

class Writer(
    val id: Id<Member, UUID>,
    val nickname: String
) {
}