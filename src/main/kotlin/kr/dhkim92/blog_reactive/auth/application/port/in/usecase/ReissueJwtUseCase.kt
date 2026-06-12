package kr.dhkim92.blog_reactive.auth.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.auth.application.dto.LoginResult
import reactor.core.publisher.Mono

interface ReissueJwtUseCase {

    fun execute(refreshToken: String): Mono<LoginResult>
}