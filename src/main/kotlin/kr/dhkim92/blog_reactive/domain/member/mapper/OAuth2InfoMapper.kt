package kr.dhkim92.blog_reactive.domain.member.mapper

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.OAuth2Info
import kr.dhkim92.blog_reactive.domain.member.r2dbc.OAuth2InfoEntity
import java.util.UUID

object OAuth2InfoMapper {

    fun toDomain(entity: OAuth2InfoEntity): OAuth2Info {
        return OAuth2Info(
            id = Id.of<OAuth2Info, UUID>(OAuth2Info::class,entity.id!!),
            provider = entity.provider,
            userId = entity.userId,
            memberId = Id.of(Member::class, entity.memberId),
        )
    }

    fun toEntity(domain: OAuth2Info): OAuth2InfoEntity {
        return OAuth2InfoEntity(
            id = domain.id?.value,
            memberId = domain.memberId.value,
            provider = domain.provider,
            userId = domain.userId,
        )
    }
}