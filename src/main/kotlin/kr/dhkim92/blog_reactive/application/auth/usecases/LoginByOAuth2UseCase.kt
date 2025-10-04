package kr.dhkim92.blog_reactive.application.auth.usecases

import kr.dhkim92.blog_reactive.application.auth.dto.LoginResult
import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import reactor.core.publisher.Mono

interface LoginByOAuth2UseCase {

    fun login(userInfo: OAuth2UserInfo): Mono<LoginResult>
}