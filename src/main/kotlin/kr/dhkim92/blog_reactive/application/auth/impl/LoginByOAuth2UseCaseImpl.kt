package kr.dhkim92.blog_reactive.application.auth.impl

import kr.dhkim92.blog_reactive.application.auth.dto.LoginResult
import kr.dhkim92.blog_reactive.application.auth.jwt.JwtService
import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.auth.usecases.LoginByOAuth2UseCase
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.domain.member.OAuth2Info
import kr.dhkim92.blog_reactive.domain.member.RefreshToken
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.Instant

@Service
@Transactional
class LoginByOAuth2UseCaseImpl(
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService
): LoginByOAuth2UseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun login(userInfo: OAuth2UserInfo): Mono<LoginResult> {
        logger.debug("loginByOAuth2UserInfo called")
        return memberRepository.findByOAuth2UserId(userInfo.getUserId())
            .switchIfEmpty(Mono.error(UnauthorizedException(message = "No member associated with the given OAuth2 user ID.")))
            .flatMap { member ->
                val jwt = jwtService.generateRefreshToken(member)
                val exp = jwtService.getRefreshTokenExpiry()
                member.addRefreshToken(
                    requester = member,
                    token = RefreshToken(
                        memberId = member.id!!,
                        token = jwt,
                        expireAt = Instant.now().plusMillis(exp))
                )
                memberRepository.save(member)
                    .map { jwt }  // save 완료 후 jwt 반환
            }.map { jwt ->
                LoginResult(
                    accessToken = "",
                    refreshToken = jwt,
                )
            }
    }
}