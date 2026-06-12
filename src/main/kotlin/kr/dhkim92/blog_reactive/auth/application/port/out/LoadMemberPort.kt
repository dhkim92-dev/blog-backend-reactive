package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Mono
import java.util.UUID

interface LoadMemberPort {
    fun findById(id: Id<AuthMember, UUID>): Mono<AuthMember>
}