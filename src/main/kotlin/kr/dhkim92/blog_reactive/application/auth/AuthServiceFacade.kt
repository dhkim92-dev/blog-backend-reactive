package kr.dhkim92.blog_reactive.application.auth

import kr.dhkim92.blog_reactive.application.auth.dto.LoginCommand
import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.auth.usecases.LoginByEmailPasswordUseCase
import kr.dhkim92.blog_reactive.application.auth.usecases.LoginByOAuth2UseCase
import kr.dhkim92.blog_reactive.application.auth.usecases.LogoutUseCase
import kr.dhkim92.blog_reactive.application.auth.usecases.ReissueJwtUseCase
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class AuthServiceFacade(
    private val loginByEmailPasswordUseCase: LoginByEmailPasswordUseCase,
    private val loginByOAuth2UseCase: LoginByOAuth2UseCase,
    private val reissueJwtUseCase: ReissueJwtUseCase,
    private val logoutUseCase: LogoutUseCase
) {

    fun loginByEmailPassword(command: LoginCommand) = loginByEmailPasswordUseCase.login(command)

    fun loginByOAuth2UserInfo(userInfo: OAuth2UserInfo) = loginByOAuth2UseCase.login(userInfo)

    fun reissueJwt(refreshToken: String) = reissueJwtUseCase.reissue(refreshToken)

    fun logout(loginId: Id<Member, UUID>, refreshToken: String) =
        logoutUseCase.logout(loginId, refreshToken)
}