package kr.dhkim92.blog_reactive.auth.adapter.out.persistence.r2dbc

import kr.dhkim92.blog_reactive.auth.domain.AuthAccountStatus
import kr.dhkim92.blog_reactive.auth.domain.AuthRole
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID


@Table(name = "auth_account")
class AuthAccountEntity(
    @Id
    @Column("id")
    var id: UUID? = null,
    @Column(value = "role")
    var role: AuthRole = AuthRole.GUEST,
    @Column(value = "status")
    var status: AuthAccountStatus = AuthAccountStatus.ACTIVE,
    @Column(value = "created_at")
    var createdAt: Instant = Instant.now(),
    @Column(value = "updated_at")
    var updatedAt: Instant = Instant.now(),
    @Column(value = "is_deleted")
    var isDeleted: Boolean = false,
) {

}