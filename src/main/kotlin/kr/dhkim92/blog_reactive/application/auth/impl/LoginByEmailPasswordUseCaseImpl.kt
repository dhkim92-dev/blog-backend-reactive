package kr.dhkim92.blog_reactive.application.auth.impl

import kr.dhkim92.blog_reactive.application.auth.dto.LoginCommand
import kr.dhkim92.blog_reactive.application.auth.dto.LoginResult
import kr.dhkim92.blog_reactive.application.auth.jwt.JwtService
import kr.dhkim92.blog_reactive.application.auth.usecases.LoginByEmailPasswordUseCase
import kr.dhkim92.blog_reactive.common.error.NotFoundException
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.domain.member.RefreshToken
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional
class LoginByEmailPasswordUseCaseImpl(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
): LoginByEmailPasswordUseCase {

    override fun login(command: LoginCommand): Mono<LoginResult> {
        return memberRepository.findByEmail(command.email)
            .switchIfEmpty(Mono.error(NotFoundException("Member not found with email: ${command.email}")))
            .flatMap { member ->
                if (!passwordEncoder.matches(command.password, member.password)) {
                    return@flatMap Mono.error(UnauthorizedException("Invalid password"))
                }

                val accessToken = jwtService.generateAccessToken(member)
                val refreshToken = jwtService.generateRefreshToken(member)

                // 리프레시 토큰 추가
                member.addRefreshToken(member, RefreshToken(
                    memberId = member.id!!,
                    token = refreshToken,
                    expireAt = jwtService.decodeRefreshToken(refreshToken).expiresAt.toInstant()
                ))

                // 멤버 저장 후 LoginResult 반환
                memberRepository.save(member)
                    .map { savedMember ->
                        LoginResult(
                            type = "Bearer",
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }
            }
    }
}