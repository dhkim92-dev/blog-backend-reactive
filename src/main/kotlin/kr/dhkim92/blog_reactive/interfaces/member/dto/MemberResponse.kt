package kr.dhkim92.blog_reactive.interfaces.member.dto

import kr.dhkim92.blog_reactive.application.member.dto.MemberDto
import kr.dhkim92.blog_reactive.common.response.BaseResponse
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID

class MemberResponse(
    val id: UUID,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isBlocked: Boolean
): BaseResponse() {

    companion object {

        fun from(dto: Mono<MemberDto>): Mono<MemberResponse> {
            return dto.map {
                MemberResponse(
                    id = it.id,
                    email = it.email,
                    nickname = it.nickname,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    isBlocked = it.isBlocked
                )
            }
        }

        fun from(dto: MemberDto): MemberResponse {
            return MemberResponse(
                id = dto.id,
                email = dto.email,
                nickname = dto.nickname,
                createdAt = dto.createdAt,
                updatedAt = dto.updatedAt,
                isBlocked = dto.isBlocked
            )
        }
    }
}