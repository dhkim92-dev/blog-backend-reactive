package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.AuthAccountRepository
import kr.dhkim92.blog_reactive.auth.application.port.out.SaveAuthAccountPort
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class R2dbcSaveAuthAccountAdapter(
    private val authAccountMapper: AuthAccountMapper,
    private val authAccountRepository: AuthAccountRepository
) : SaveAuthAccountPort {

    override fun save(account: AuthAccount): Mono<AuthAccount> {
        val entity = authAccountMapper.toR2dbc(account)
        return authAccountRepository.save(entity)
            .map(authAccountMapper::fromR2dbc)
    }
}