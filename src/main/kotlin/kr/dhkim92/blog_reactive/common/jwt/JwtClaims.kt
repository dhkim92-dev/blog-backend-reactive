package kr.dhkim92.blog_reactive.common.jwt

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.auth.domain.AuthRole

class JwtClaims(
    val sub: String,
    val memberId: String? = null,
    val nickname: String? = null,
    val role: AuthRole = AuthRole.GUEST
){

}