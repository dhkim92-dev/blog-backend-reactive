package kr.dhkim92.blog_reactive.domain.member.mapper

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.r2dbc.MemberEntity
import kr.dhkim92.blog_reactive.domain.member.r2dbc.OAuth2InfoEntity
import kr.dhkim92.blog_reactive.domain.member.r2dbc.RefreshTokenEntity
import reactor.core.publisher.Flux
import java.util.UUID

object MemberMapper {

    fun toDomain(entity: MemberEntity,
                 refreshTokens: Set<RefreshTokenEntity>,
                 oauth2Infos: Set<OAuth2InfoEntity> ): Member {
        return Member(
            id = Id.of(Member::class, entity.id!!),
            nickname = entity.nickname,
            email = entity.email,
            role = entity.role,
            _oauth2Infos = oauth2Infos.map { OAuth2InfoMapper.toDomain(it) }.toMutableSet(),
            _refreshTokens = refreshTokens.map { RefreshTokenMapper.toDomain(it) }.toMutableSet(),
            password = entity.password,
            isBlocked = entity.isBlocked,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted,
        )
    }

    fun toEntity(domain: Member): MemberEntity {
        return MemberEntity(
            id = domain.id?.value,
            email = domain.email,
            nickname = domain.nickname,
            password = domain.password,
            role = domain.role,
            isBlocked = domain.isBlocked,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            isDeleted = domain.isDeleted,
        )
    }
}