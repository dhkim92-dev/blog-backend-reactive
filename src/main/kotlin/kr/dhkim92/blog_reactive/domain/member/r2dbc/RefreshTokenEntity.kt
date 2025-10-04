package kr.dhkim92.blog_reactive.domain.member.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "refresh_token")
class RefreshTokenEntity(
    @Id
    var id: UUID? = null,
    @Column("member_id")
    var memberId: UUID = UUID.randomUUID(),
    @Column("token")
    var token: String = UUID.randomUUID().toString(),
    @Column("expire_at")
    var expireAt: Instant = Instant.now().plusSeconds(60 * 60 * 24 * 7), // 7 days
    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    @Column("is_deleted")
    var isDeleted: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RefreshTokenEntity) return false
        if (id != other.id) return false
        if (token != other.token) return false
        return true
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}