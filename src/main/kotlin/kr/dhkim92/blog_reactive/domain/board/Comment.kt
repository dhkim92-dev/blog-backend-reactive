package kr.dhkim92.blog_reactive.domain.board

import kr.dhkim92.blog_reactive.common.error.BadRequestException
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

class Comment(
    val id: Id<Comment, UUID>? = null,
    val parentId: Id<Comment, UUID>? = null,
    articleId: Id<Article, UUID>,
    writerId: Id<Member, UUID>,
    content: String,
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    isDeleted: Boolean = false,
    replyCount: Int = 0,
    depth: Int = 0
): BaseDomainEntity(
    createdAt = createdAt,
    updatedAt = updatedAt,
    isDeleted = isDeleted,

) {

    val articleId: Id<Article, UUID> = articleId

    val writerId: Id<Member, UUID> = writerId

    var content: String = content
        protected set

    var replyCount: Int = replyCount
        protected set

    var depth: Int = depth
        protected set

    fun updateContent(requester: Member, newContent: String) {
        require(!isDeleted)  { "Cannot update a deleted comment." }
        require(newContent.isNotEmpty() && newContent.length <= 500) { "Content must be between 1 and 500 characters." }
        checkPermissions(requester)
        this.content = newContent
    }

    fun increaseReplyCount() {
        this.replyCount += 1
    }

    fun decreaseReplyCount() {
        if (this.replyCount > 0) {
            this.replyCount -= 1
        }
    }

    private fun checkPermissions(requester: Member) {
        if (!requester.isAdmin() && this.writerId != requester.id) {
            throw IllegalAccessException("You do not have permission to modify this comment.")
        }
        if ( requester.isBlocked || requester.isDeleted ) {
            throw IllegalAccessException("Blocked or deleted members cannot modify comments.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Comment) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        fun create(
            writer: Member,
            article: Article,
            parent: Comment?,
            content: String
        ): Comment {
            require(content.isNotEmpty() && content.length <= 500) { "Content must be between 1 and 500 characters." }
            if ( writer.isBlocked || writer.isDeleted ) {
                throw ForbiddenException(message="Blocked or deleted members cannot create comments.")
            }

            if ( article.isDeleted ) {
                throw BadRequestException(message="Cannot comment on a deleted article.")
            }

            val rootParent = parent?.let {
                if (it.isDeleted) {
                    throw BadRequestException(message="Cannot reply to a deleted comment.")
                }
                var p = it
                while (p.parentId != null) {
                    p = p // Replace with actual fetching logic
                }
                p
            }

            val depth = rootParent?.let { it.depth + 1 } ?: 0

            return Comment(
                id = null,
                parentId = parent?.id,
                articleId = article.id!!,
                writerId = writer.id!!,
                content = content,
                depth = depth
            )
        }
    }
}