package kr.dhkim92.blog_reactive.domain.member.r2dbc

import kr.dhkim92.blog_reactive.domain.member.OAuth2Provider
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "oauth2_member")
class OAuth2InfoEntity(
    @Id
    var id: UUID? = null,
    @Column("member_id")
    var memberId: UUID = UUID.randomUUID(),
    @Column("provider")
    var provider: OAuth2Provider = OAuth2Provider.GITHUB,
    @Column("user_id")
    var userId: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OAuth2InfoEntity) return false
        if (id != other.id) return false
        if (provider != other.provider) return false
        if (userId != other.userId) return false
        return true
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}