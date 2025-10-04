package kr.dhkim92.blog_reactive.application.auth.dto

data class LoginCommand (
    val email: String,
    val password: String
){

}