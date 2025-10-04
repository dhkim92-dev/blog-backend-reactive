package kr.dhkim92.blog_reactive.application.auth.impl

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import kr.dhkim92.blog_reactive.application.auth.dto.LoginResult
import kr.dhkim92.blog_reactive.application.auth.jwt.JwtService
import kr.dhkim92.blog_reactive.application.auth.usecases.ReissueJwtUseCase
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.RefreshToken
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class ReissueJwtUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService
): ReissueJwtUseCase {

    @Transactional
    override fun reissue(refreshToken: String): Mono<LoginResult> {
        return Mono.fromCallable {
                // 1. 입력으로 주어진 refreshToken 검증
                val decodedJWT = jwtService.verifyRefreshToken(refreshToken)
                val memberId = Id.of<Member, UUID>(Member::class, UUID.fromString(decodedJWT.subject))
                memberId
            }
            .onErrorResume { e ->
                // JWTVerificationException 발생 시 에러 전파
                when (e) {
                    is JWTVerificationException -> Mono.error(JWTVerificationException("Invalid refresh token"))
                    else -> Mono.error(e)
                }
            }
            .flatMap { memberId ->
                // 2. Member ID를 이용하여 사용자 정보 조회
                memberRepository.findById(memberId)
                    .switchIfEmpty(Mono.error(NotFoundException("Member not found")))
            }
            .flatMap { member ->
                // 3. 주어진 토큰이 member.refreshToken에 존재하는지 확인
                val foundToken = member.refreshTokens.find { it.token == refreshToken && !it.isDeleted }

                if (foundToken == null) {
                    return@flatMap Mono.error<LoginResult>(UnauthorizedException("Refresh token not found or invalid"))
                }

                // 4. 새로운 AccessToken 발급 및 필요시 RefreshToken 재발급
                val newAccessToken = jwtService.generateAccessToken(member)
                val oneDayFromNow = Instant.now().plus(1, ChronoUnit.DAYS)

                // RefreshToken 만료 시간이 1일 이내로 남아있다면 재발급
                if (foundToken.expireAt.isBefore(oneDayFromNow)) {
                    // 새 RefreshToken 생성
                    val newRefreshToken = jwtService.generateRefreshToken(member)
                    val newRefreshTokenObj = RefreshToken(
                        memberId = member.id!!,
                        token = newRefreshToken
                    )

                    member.addRefreshToken(member, newRefreshTokenObj)
                    // 업데이트된 Member 저장
                    memberRepository.save(member)
                        .map { LoginResult(accessToken = newAccessToken, refreshToken = newRefreshToken) }
                } else {
                    // RefreshToken 유효기간이 충분하면 AccessToken만 재발급
                    Mono.just(LoginResult(accessToken = newAccessToken, refreshToken = refreshToken))
                }
            }
    }
}