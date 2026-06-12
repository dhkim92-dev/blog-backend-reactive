package kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.auth.application.dto.LoginResult
import kr.dhkim92.blog_reactive.auth.application.oauth2.dto.OAuth2UserInfo
import reactor.core.publisher.Mono

interface LoginByOAuth2UseCase {

    fun execute(userInfo: OAuth2UserInfo): Mono<LoginResult>
}