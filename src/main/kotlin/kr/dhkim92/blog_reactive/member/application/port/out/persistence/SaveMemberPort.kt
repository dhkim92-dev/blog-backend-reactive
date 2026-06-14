package kr.dhkim92.blog_reactive.member.application.port.out.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.member.domain.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface SaveMemberPort {

    fun save(member: Member): Mono<Member>

    fun deleteById(id: Id<Member, UUID>): Mono<Void>
}