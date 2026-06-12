package kr.dhkim92.blog_reactive.domain.member.r2dbc

import kr.dhkim92.blog_reactive.auth.domain.AuthRole
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime
import java.util.UUID


@Table(name = "member")
class MemberEntity(
    @Id
    @Column("id")
    var id: UUID? = null,
    @Column("email")
    var email: String,
    @Column("password")
    var password: String,
    @Column("nickname")
    var nickname: String,
    @Column("role")
    var role: AuthRole = AuthRole.MEMBER,
    @Column("is_blocked")
    var isBlocked: Boolean = false,
    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column("is_deleted")
    var isDeleted: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MemberEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
