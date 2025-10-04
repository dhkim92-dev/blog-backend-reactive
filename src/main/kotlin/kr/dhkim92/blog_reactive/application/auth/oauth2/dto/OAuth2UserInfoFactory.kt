package kr.dhkim92.blog_reactive.application.auth.oauth2.dto

abstract class OAuth2UserInfoFactory {

    abstract fun supports(registrationId: String): Boolean

    abstract fun create(attributes: Map<String, Any>): OAuth2UserInfo
}