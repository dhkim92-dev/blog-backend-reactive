package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(name = "email_password_credential")
class EmailPasswordCredentialEntity(
    @Id
    @Column(value = "id")
    var id: UUID? = null,
    @Column(value = "auth_account_id")
    var authAccountId: UUID,
    @Column(value = "email")
    var email: String,
    @Column(value = "password")
    var password: String,
) {

}