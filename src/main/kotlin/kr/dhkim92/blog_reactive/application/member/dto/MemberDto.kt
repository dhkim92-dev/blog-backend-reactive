package kr.dhkim92.blog_reactive.application.member.dto

import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.MemberRole
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID

class MemberDto(
    val id: UUID,
    val email: String,
    val nickname: String,
    val role: MemberRole,
    val isBlocked: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    companion object {
        fun from(entity: Member): MemberDto {
            return MemberDto(
                id = entity.id!!.value,
                email = entity.email,
                nickname = entity.nickname,
                role = entity.role,
                isBlocked = entity.isBlocked,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}