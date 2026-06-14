package kr.dhkim92.blog_reactive.member.adapter.out.persistence.r2dbc

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID


@Table("member")
class MemberEntity(
    @Id
    @Column("id")
    var id: UUID? = null,
    @Column("auth_account_id")
    var authAccountId: UUID = UUID.randomUUID(),
    @Column("nickname")
    var nickname: String = "",
    @CreatedDate
    @Column("created_at")
    var createdAt: Instant = Instant.now(),
    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: Instant = Instant.now(),
    @Column("is_deleted")
    var isDeleted: Boolean = false
) {
}