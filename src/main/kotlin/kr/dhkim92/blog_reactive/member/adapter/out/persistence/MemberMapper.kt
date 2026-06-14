package kr.dhkim92.blog_reactive.member.adapter.out.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import kr.dhkim92.blog_reactive.member.adapter.out.persistence.r2dbc.MemberEntity
import kr.dhkim92.blog_reactive.member.domain.Member
import org.springframework.stereotype.Component

@Component
class MemberMapper {

    fun toR2dbc(entity: Member): MemberEntity {
        return MemberEntity(
            id = entity.id?.value,
            authAccountId = entity.authAccountId.value,
            nickname = entity.nickname,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted
        )
    }

    fun fromR2dbc(entity: MemberEntity): Member {
        return Member(
            id = Id.of(Member::class, entity.id!!),
            authAccountId = Id.of(AuthInfo::class, entity.authAccountId),
            nickname = entity.nickname,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted
        )
    }
}