package kr.dhkim92.blog_reactive.auth.application.port.out

import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.common.entity.Id
import reactor.core.publisher.Mono
import java.util.UUID

interface LoadMemberPort {

    fun findByAuthAccountId(authAccountId: Id<AuthAccount, UUID>): Mono<AuthMember>
}