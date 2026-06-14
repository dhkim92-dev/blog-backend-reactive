package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(name = "auth_session")
class AuthSessionEntity(
    @Id
    @Column("id")
    var id: UUID? = null,
    @Column("auth_account_id")
    var authAccountId: UUID,
    @Column("device_name")
    var deviceName:String? = null,
    @Column("token")
    var token: String,
    @Column("issued_at")
    var issuedAt: Instant,
    @Column("expires_at")
    var expiresAt: Instant,
)