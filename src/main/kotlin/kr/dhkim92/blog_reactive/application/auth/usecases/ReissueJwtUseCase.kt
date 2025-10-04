package kr.dhkim92.blog_reactive.application.auth.usecases

import kr.dhkim92.blog_reactive.application.auth.dto.LoginResult
import reactor.core.publisher.Mono

interface ReissueJwtUseCase {

    fun reissue(refreshToken: String): Mono<LoginResult>
}