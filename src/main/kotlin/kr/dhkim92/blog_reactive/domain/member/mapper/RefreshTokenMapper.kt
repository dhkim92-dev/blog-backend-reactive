package kr.dhkim92.blog_reactive.domain.member.mapper

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.RefreshToken
import kr.dhkim92.blog_reactive.domain.member.r2dbc.RefreshTokenEntity
import java.util.UUID

object RefreshTokenMapper {

    fun toDomain(entity: RefreshTokenEntity): RefreshToken {
        return RefreshToken(
            id = Id.of(RefreshToken::class, entity.id!!),
            memberId = Id.of(Member::class, entity.memberId),
            token = entity.token,
            expireAt = entity.expireAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted,
        )
    }

    fun toEntity(domain: RefreshToken): RefreshTokenEntity {
        return RefreshTokenEntity(
            id = domain.id?.value,
            memberId = domain.memberId.value,
            token = domain.token,
            expireAt = domain.expireAt,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            isDeleted = domain.isDeleted,
        )
    }
}