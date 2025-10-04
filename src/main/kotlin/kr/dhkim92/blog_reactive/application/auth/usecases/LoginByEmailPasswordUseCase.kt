package kr.dhkim92.blog_reactive.application.auth.usecases

import kr.dhkim92.blog_reactive.application.auth.dto.LoginCommand
import kr.dhkim92.blog_reactive.application.auth.dto.LoginResult
import reactor.core.publisher.Mono

interface LoginByEmailPasswordUseCase {

    fun login(command: LoginCommand): Mono<LoginResult>
}