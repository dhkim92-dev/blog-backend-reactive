package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthAccountRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.LoadAuthAccountPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class R2dbcLoadAuthAccountAdapter(
    private val authAccountRepository: AuthAccountRepository,
    private val authAccountMapper: AuthAccountMapper
) : LoadAuthAccountPort {

    override fun findById(id: Id<AuthAccount, UUID>): Mono<AuthAccount> {
        return authAccountRepository.findById(id.value)
            .map { entity -> authAccountMapper.fromR2dbc(entity) }
    }
}