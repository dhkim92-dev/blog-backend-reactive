package kr.dhkim92.blog_reactive.application.auth.oauth2.dto

import kr.dhkim92.blog_reactive.domain.member.OAuth2Provider

abstract class OAuth2UserInfo(
    val attributes: Map<String, Any>
) {

    abstract fun getId(): String

    abstract fun getNickName(): String

    abstract fun getEmail(): String

    abstract fun getProvider(): OAuth2Provider

    fun getUserId(): String {
        return "${getProvider().name.lowercase()}:${getId()}"
    }

    override fun toString(): String {
        return "OAuth2UserInfo(provider=${getProvider().name}, id=${getId()}, nickname=${getNickName()}, email=${getEmail()})"
    }
}