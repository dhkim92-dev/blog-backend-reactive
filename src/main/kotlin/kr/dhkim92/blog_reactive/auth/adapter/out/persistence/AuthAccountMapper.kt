package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthAccountEntity
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AuthAccountMapper {

    fun fromR2dbc(entity: AuthAccountEntity): AuthAccount {
        return AuthAccount(
            id = Id<AuthAccount, UUID>(AuthAccount::class, entity.id!!),
            role = entity.role,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted
        )
    }

    fun toR2dbc(account: AuthAccount): AuthAccountEntity {
        return AuthAccountEntity(
            id = account.identifier.value,
            role = account.role,
            status = account.status,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt,
            isDeleted = account.isDeleted
        )
    }
}