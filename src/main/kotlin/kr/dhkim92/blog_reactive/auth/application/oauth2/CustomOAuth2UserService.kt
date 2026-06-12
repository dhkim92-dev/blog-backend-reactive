package kr.dhkim92.blog_reactive.auth.application.oauth2

import kr.dhkim92.blog_reactive.auth.application.AuthServiceFacade
import kr.dhkim92.blog_reactive.auth.application.oauth2.dto.CustomOAuth2User
import kr.dhkim92.blog_reactive.auth.application.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.auth.application.oauth2.dto.OAuth2UserInfoFactory
import kr.dhkim92.blog_reactive.application.member.MemberServiceFacade
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomOAuth2UserService(
    private val memberServiceFacade: MemberServiceFacade,
    private val userInfoFactories: List<OAuth2UserInfoFactory>,
    private val authServiceFacade: AuthServiceFacade
): ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private val delegate = DefaultReactiveOAuth2UserService()

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadUser(userRequest: OAuth2UserRequest): Mono<OAuth2User?>? {
        logger.debug("Load OAuth2 User...")
        return delegate.loadUser(userRequest)
            .map { oAuth2User ->
                val registrationId = userRequest.clientRegistration.registrationId
                val userInfoFactory = userInfoFactories.firstOrNull { it.supports(registrationId) }
                    ?: throw IllegalArgumentException("Unsupported OAuth2 provider: $registrationId")
                userInfoFactory.create(oAuth2User.attributes)
            }.doOnSuccess { logger.debug("Get OAuth2User : ${it}") }
            .flatMap { userInfo ->
                processOAuth2User(userInfo)
                    .doOnSuccess { logger.debug("loginResult : ${it.refreshToken}" ) }
            }
    }

    private fun processOAuth2User(userInfo: OAuth2UserInfo): Mono<CustomOAuth2User> {
        logger.debug("Process OAuth2 User...")
        return memberServiceFacade.getMemberByOAuth2UserInfo(userInfo)
            .onErrorResume {
                logger.debug("No member found with OAuth2 user ID: ${userInfo.getUserId()}, creating new member.")
                memberServiceFacade.createMemberByOAuth2UserInfo(userInfo)
            }
            .flatMap { memberDto ->
                logger.debug("Logging in member with ID: {}", memberDto.id)
                authServiceFacade.loginByOAuth2UserInfo(userInfo)
            }
            .map {
                CustomOAuth2User(
                    userInfo = userInfo,
                    refreshToken = it.refreshToken,
                )
            }
    }
}

