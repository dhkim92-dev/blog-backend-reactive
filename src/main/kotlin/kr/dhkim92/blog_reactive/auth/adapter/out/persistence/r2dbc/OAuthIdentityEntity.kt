package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import kr.dhkim92.blog_reactive.auth.domain.OAuthProvider
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name="oauth_identity")
class OAuthIdentityEntity(
    @Id
    @Column("id")
    var id: UUID? = null,
    @Column("auth_account_id")
    var authAccountId: UUID,
    @Column("provider")
    val provider: OAuthProvider,
    @Column("provider_user_id")
    val providerUserId: String,
) {
}