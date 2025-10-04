package kr.dhkim92.blog_reactive.domain.board.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "article_category")
class CategoryEntity(
    @Id
    var id: Long? = null,
    @Column("name")
    var name: String,
    @Column("count")
    var count: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        return if (other is CategoryEntity) {
            other.id == this.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}