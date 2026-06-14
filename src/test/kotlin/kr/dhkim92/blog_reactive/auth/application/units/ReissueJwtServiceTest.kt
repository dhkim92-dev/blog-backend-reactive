package kr.dhkim92.blog_reactive.auth.application.units

import com.auth0.jwt.interfaces.DecodedJWT
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.dhkim92.blog_reactive.common.jwt.JwtService
import kr.dhkim92.blog_reactive.auth.application.ReissueJwtService
import kr.dhkim92.blog_reactive.common.jwt.JwtClaims
import kr.dhkim92.blog_reactive.common.jwt.JwtToken
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthAccountPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthSessionPort
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadMemberPort
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveAuthSessionPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.auth.domain.exceptions.NotActiveAuthAccountException
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.UnauthorizedException
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.Date
import java.util.UUID

class ReissueJwtServiceTest : BehaviorSpec({

    Given("refresh token 재발급") {

        When("세션에서 refresh token을 찾지 못하면") {
            Then("UnauthorizedException이 발생한다") {
                val fixture = Fixture()
                val refreshToken = "refresh-token"
                val decoded = fixture.decodedJwt(expiresAt = Instant.now().plusSeconds(3600))

                every { fixture.jwtService.decodeRefreshToken(refreshToken) } returns decoded
                every { fixture.loadAuthAccountPort.findByMemberId(fixture.memberId) } returns Mono.just(fixture.authAccount)
                every { fixture.loadMemberPort.findById(fixture.authMemberId) } returns Mono.just(fixture.authMember)
                every { fixture.loadAuthSessionPort.findByAuthAccountId(fixture.authAccountId) } returns Flux.empty()

                StepVerifier.create(fixture.service.execute(refreshToken))
                    .expectErrorSatisfies { throwable ->
                        val error = throwable.shouldBeInstanceOf<UnauthorizedException>()
                        error.message shouldBe "Refresh token not found or invalid"
                    }
                    .verify()

                verify(exactly = 0) { fixture.saveAuthSessionPort.delete(any()) }
            }
        }

        When("계정이 비활성 상태이면") {
            Then("NotActiveAuthAccountException이 발생한다") {
                val fixture = Fixture().also { it.authAccount.markAsBlock() }
                val refreshToken = "refresh-token"
                val decoded = fixture.decodedJwt(expiresAt = Instant.now().plusSeconds(3600))

                every { fixture.jwtService.decodeRefreshToken(refreshToken) } returns decoded
                every { fixture.loadAuthAccountPort.findByMemberId(fixture.memberId) } returns Mono.just(fixture.authAccount)

                StepVerifier.create(fixture.service.execute(refreshToken))
                    .expectErrorSatisfies { throwable ->
                        throwable.shouldBeInstanceOf<NotActiveAuthAccountException>()
                    }
                    .verify()

                verify(exactly = 0) { fixture.loadMemberPort.findById(any()) }
                verify(exactly = 0) { fixture.loadAuthSessionPort.findByAuthAccountId(any()) }
            }
        }

        When("refresh token이 만료되면") {
            Then("세션을 삭제하고 UnauthorizedException이 발생한다") {
                val fixture = Fixture()
                val refreshToken = "refresh-token"
                val decoded = fixture.decodedJwt(expiresAt = Instant.now().minusSeconds(60))

                every { fixture.jwtService.decodeRefreshToken(refreshToken) } returns decoded
                every { fixture.loadAuthAccountPort.findByMemberId(fixture.memberId) } returns Mono.just(fixture.authAccount)
                every { fixture.loadMemberPort.findById(fixture.authMemberId) } returns Mono.just(fixture.authMember)
                every { fixture.loadAuthSessionPort.findByAuthAccountId(fixture.authAccountId) } returns Flux.just(fixture.authSession)
                every { fixture.saveAuthSessionPort.delete(fixture.authSession) } returns Mono.just(Unit)

                StepVerifier.create(fixture.service.execute(refreshToken))
                    .expectErrorSatisfies { throwable ->
                        val error = throwable.shouldBeInstanceOf<UnauthorizedException>()
                        error.message shouldBe "Refresh token expired"
                    }
                    .verify()

                verify(exactly = 1) { fixture.saveAuthSessionPort.delete(fixture.authSession) }
                verify(exactly = 0) { fixture.jwtService.verifyRefreshToken(any()) }
            }
        }

        When("refresh token 만료가 1일 이내면") {
            Then("refresh token을 rotate하고 저장 후 새 refresh token을 반환한다") {
                val fixture = Fixture()
                val refreshToken = "refresh-token"
                val decoded = fixture.decodedJwt(expiresAt = Instant.now().plusSeconds(60 * 60 * 12))
                val newAccessToken = JwtToken(
                    token = "new-access-token",
                    issuedAt = Instant.parse("2026-06-01T00:00:00Z"),
                    expiresAt = Instant.parse("2026-06-01T00:30:00Z")
                )
                val newRefreshToken = JwtToken(
                    token = "new-refresh-token",
                    issuedAt = Instant.parse("2026-06-01T00:00:00Z"),
                    expiresAt = Instant.parse("2026-06-15T00:00:00Z")
                )

                every { fixture.jwtService.decodeRefreshToken(refreshToken) } returns decoded
                every { fixture.loadAuthAccountPort.findByMemberId(fixture.memberId) } returns Mono.just(fixture.authAccount)
                every { fixture.loadMemberPort.findById(fixture.authMemberId) } returns Mono.just(fixture.authMember)
                every { fixture.loadAuthSessionPort.findByAuthAccountId(fixture.authAccountId) } returns Flux.just(fixture.authSession)
                every { fixture.jwtService.generateAccessToken(any<JwtClaims>()) } returns newAccessToken
                every { fixture.jwtService.verifyRefreshToken(refreshToken) } returns decoded
                every { fixture.jwtService.generateRefreshToken(any<JwtClaims>()) } returns newRefreshToken
                every { fixture.saveAuthSessionPort.save(fixture.authSession) } returns Mono.just(fixture.authSession)

                StepVerifier.create(fixture.service.execute(refreshToken))
                    .assertNext { result ->
                        result.accessToken shouldBe "new-access-token"
                        result.refreshToken shouldBe "new-refresh-token"
                    }
                    .verifyComplete()

                fixture.authSession.token shouldBe "new-refresh-token"
                verify(exactly = 1) { fixture.saveAuthSessionPort.save(fixture.authSession) }
                verify(exactly = 0) { fixture.saveAuthSessionPort.delete(any()) }
            }
        }

        When("refresh token 유효 기간이 충분하면") {
            Then("access token만 재발급하고 기존 refresh token을 반환한다") {
                val fixture = Fixture()
                val refreshToken = "refresh-token"
                val decoded = fixture.decodedJwt(expiresAt = Instant.now().plusSeconds(60 * 60 * 24 * 3))
                val newAccessToken = JwtToken(
                    token = "new-access-token",
                    issuedAt = Instant.parse("2026-06-01T00:00:00Z"),
                    expiresAt = Instant.parse("2026-06-01T00:30:00Z")
                )

                every { fixture.jwtService.decodeRefreshToken(refreshToken) } returns decoded
                every { fixture.loadAuthAccountPort.findByMemberId(fixture.memberId) } returns Mono.just(fixture.authAccount)
                every { fixture.loadMemberPort.findById(fixture.authMemberId) } returns Mono.just(fixture.authMember)
                every { fixture.loadAuthSessionPort.findByAuthAccountId(fixture.authAccountId) } returns Flux.just(fixture.authSession)
                every { fixture.jwtService.generateAccessToken(any<JwtClaims>()) } returns newAccessToken
                every { fixture.jwtService.verifyRefreshToken(refreshToken) } returns decoded

                StepVerifier.create(fixture.service.execute(refreshToken))
                    .assertNext { result ->
                        result.accessToken shouldBe "new-access-token"
                        result.refreshToken shouldBe refreshToken
                    }
                    .verifyComplete()

                verify(exactly = 0) { fixture.jwtService.generateRefreshToken(any<JwtClaims>()) }
                verify(exactly = 0) { fixture.saveAuthSessionPort.save(any()) }
                verify(exactly = 0) { fixture.saveAuthSessionPort.delete(any()) }
            }
        }
    }
}) {

    private class Fixture {
        val loadAuthSessionPort: LoadAuthSessionPort = mockk()
        val saveAuthSessionPort: SaveAuthSessionPort = mockk()
        val loadAuthAccountPort: LoadAuthAccountPort = mockk()
        val loadMemberPort: LoadMemberPort = mockk()
        val jwtService: JwtService = mockk()

        val memberUuid: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val authMemberId: Id<AuthMember, UUID> = Id.of(AuthMember::class, memberUuid)
        val memberId: Id<Member, UUID> = Id.of(Member::class, memberUuid)
        val authAccountId: Id<AuthAccount, UUID> = Id.of(
            AuthAccount::class,
            UUID.fromString("22222222-2222-2222-2222-222222222222")
        )
        val authSessionId: Id<AuthSession, UUID> = Id.of(
            AuthSession::class,
            UUID.fromString("33333333-3333-3333-3333-333333333333")
        )

        val authAccount = AuthAccount(
            id = authAccountId,
            memberId = authMemberId
        )

        val authMember = AuthMember(
            id = authMemberId,
            nickname = "tester"
        )

        val authSession = AuthSession(
            id = authSessionId,
            authAccountId = authAccountId,
            token = "refresh-token",
            issuedAt = Instant.parse("2026-05-01T00:00:00Z"),
            expiresAt = Instant.parse("2026-06-30T00:00:00Z")
        )

        val service = ReissueJwtService(
            loadAuthSessionPort = loadAuthSessionPort,
            saveAuthSessionPort = saveAuthSessionPort,
            loadAuthAccountPort = loadAuthAccountPort,
            loadMemberPort = loadMemberPort,
            jwtService = jwtService
        )

        fun decodedJwt(expiresAt: Instant): DecodedJWT {
            val decodedJwt = mockk<DecodedJWT>()
            every { decodedJwt.subject } returns memberUuid.toString()
            every { decodedJwt.expiresAt } returns Date.from(expiresAt)
            return decodedJwt
        }
    }
}
