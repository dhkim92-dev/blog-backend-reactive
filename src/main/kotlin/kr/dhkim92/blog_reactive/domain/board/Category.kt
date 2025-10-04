package kr.dhkim92.blog_reactive.domain.board

import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.domain.BaseDomainEntity
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.MemberRole
import kr.dhkim92.blog_reactive.domain.member.RefreshToken
import java.util.UUID

class Category(
    val id: Id<Category, Long>? = null,
    name: String,
    count: Int = 0
): BaseDomainEntity() {

    var name: String = name
        protected set

    var count: Int = count
        protected set

    fun increaseCount() {
        this.count++
    }

    fun decreaseCount() {
        if (this.count > 0) {
            this.count--
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Category) {
            other.id == this.id
        } else {
            false
        }
    }

    fun changeName(name: String) {
        require(name.isNotBlank()) { "Category name must not be blank" }
        this.name = name
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        fun create(requester: Member, name: String): Category {
            if ( requester.role != MemberRole.ADMIN ) {
                throw ForbiddenException()
            }
            return Category(
                name = name
            )
        }
    }
}