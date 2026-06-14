package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthSessionEntity
import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthSessionRepository
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthSession
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Component

@Component
class AuthSessionMapper(
    private val authSessionRepository: AuthSessionRepository
) {

    fun fromR2dbc(sessionEntity: AuthSessionEntity): AuthSession {
        return AuthSession(
            id = Id.of(AuthSession::class, sessionEntity.id!!),
            authAccountId = Id.of(AuthAccount::class, sessionEntity.authAccountId),
            token = sessionEntity.token,
            issuedAt = sessionEntity.issuedAt,
            expiresAt = sessionEntity.expiresAt,
            deviceName = sessionEntity.deviceName
        )
    }

    fun toR2dbc(session: AuthSession): AuthSessionEntity {
        return AuthSessionEntity(
            id = session.id?.value,
            authAccountId = session.authAccountId.value,
            token = session.token,
            issuedAt = session.issuedAt,
            expiresAt = session.expiresAt,
            deviceName = session.deviceName
        )
    }
}