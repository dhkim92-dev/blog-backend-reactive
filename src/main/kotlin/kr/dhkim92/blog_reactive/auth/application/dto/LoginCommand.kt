package kr.dhkim92.blog_reactive.auth.application.dto

import kr.dhkim92.blog_reactive.auth.domain.Password
import kr.dhkim92.blog_reactive.common.data.Email

data class LoginCommand (
    val email: Email,
    val password: Password
){

}