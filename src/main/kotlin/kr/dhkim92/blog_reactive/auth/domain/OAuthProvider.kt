package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.error.BadRequestException
import kr.dhkim92.blog_reactive.domain.member.OAuth2Provider

enum class OAuthProvider {
    GOOGLE,
    GITHUB,
    KAKAO,
    NAVER;

    companion object {
        fun from(provider: OAuth2Provider): OAuthProvider {
            when (provider) {
                OAuth2Provider.GITHUB -> return OAuthProvider.GITHUB
                OAuth2Provider.GOOGLE -> return OAuthProvider.GOOGLE
            }
        }
    }
}