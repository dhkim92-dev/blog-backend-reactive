package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import java.util.UUID

interface AuthAccountRepository : R2dbcRepository<AuthAccountEntity, UUID> {

}