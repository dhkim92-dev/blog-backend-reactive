package kr.dhkim92.blog_reactive.auth.adapter.out

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthMemberRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadMemberPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.AuthMember
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class LoadMemberAdapter(
    private val authMemberRepository: AuthMemberRepository
): LoadMemberPort {

    override fun findByAuthAccountId(authAccountId: Id<AuthAccount, UUID>): Mono<AuthMember> {
        return authMemberRepository.findByAuthAccountId(authAccountId = authAccountId.value)
    }
}