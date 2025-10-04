package kr.dhkim92.blog_reactive.application.auth.oauth2.dto

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.MemberRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.UUID

class CustomOAuth2User(
    val userInfo: OAuth2UserInfo,
    val refreshToken: String,
    val _authorities: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(MemberRole.MEMBER.role))
): OAuth2User {

    override fun getAttributes(): Map<String, Any> {
        return userInfo.attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return _authorities
    }

    override fun getName(): String {
        return userInfo.getNickName()
    }
}