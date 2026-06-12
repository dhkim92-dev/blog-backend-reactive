package kr.dhkim92.blog_reactive.auth.application.oauth2.dto

import kr.dhkim92.blog_reactive.domain.member.OAuth2Provider
import org.springframework.stereotype.Component

@Component
class GithubOAuth2UserInfoFactory(): OAuth2UserInfoFactory() {

    override fun supports(registrationId: String): Boolean {
        return registrationId.equals(OAuth2Provider.GITHUB.name, ignoreCase = true)
    }

    override fun create(attributes: Map<String, Any>): OAuth2UserInfo {
        return GithubOAuth2UserInfo(attributes)
    }
}