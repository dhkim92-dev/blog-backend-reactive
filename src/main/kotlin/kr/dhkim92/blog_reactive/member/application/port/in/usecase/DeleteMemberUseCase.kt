package kr.dhkim92.blog_reactive.member.application.port.`in`.usecase

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.jwt.LoginMember
import kr.dhkim92.blog_reactive.member.domain.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface DeleteMemberUseCase {

    fun execute(loginId: Id<LoginMember, UUID>, resourceId: Id<Member, UUID>): Mono<Void>
}