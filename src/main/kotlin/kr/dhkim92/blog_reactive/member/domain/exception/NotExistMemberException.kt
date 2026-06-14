package kr.dhkim92.blog_reactive.member.domain.exception

import kr.dhkim92.blog_reactive.common.error.NotFoundException

class NotExistMemberException() : NotFoundException(
    code = "MEMBER_NOT_EXIST",
    message = "존재하지 않는 멤버입니다"
){
}