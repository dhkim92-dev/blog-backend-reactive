package kr.dhkim92.blog_reactive.auth.adapter.out.persistence

import kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc.EmailPasswordCredentialEntity
import kr.dhkim92.blog_reactive.auth.domain.AuthAccount
import kr.dhkim92.blog_reactive.auth.domain.EmailPasswordCredential
import kr.dhkim92.blog_reactive.common.data.Email
import kr.dhkim92.blog_reactive.common.entity.Id
import org.springframework.stereotype.Component

@Component
class EmailPasswordCredentialMapper {

    fun fromR2dbc(credential: EmailPasswordCredentialEntity): EmailPasswordCredential {
        return EmailPasswordCredential(
            id = Id.of(EmailPasswordCredential::class, credential.id!!),
            authAccountId = Id.of(AuthAccount::class, credential.authAccountId),
            email = Email(credential.email),
            password = credential.password
        )
    }

    fun toR2dbc(credential: EmailPasswordCredential): EmailPasswordCredentialEntity {
        return EmailPasswordCredentialEntity(
            id = credential.id?.value,
            authAccountId = credential.authAccountId.value,
            email = credential.email.value,
            password = credential.password,
        )
    }
}