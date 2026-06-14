package kr.dhkim92.blog_reactive.member.domain.exception

import kr.dhkim92.blog_reactive.common.error.ConflictException

class AlreadyExistMemberException : ConflictException(
    message = "이미 회원 정보가 존재합니다",
    code = "ALREADY_EXIST_MEMBER"
) {

}