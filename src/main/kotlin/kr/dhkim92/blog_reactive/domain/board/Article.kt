package kr.dhkim92.blog_reactive.domain.board

import kr.dhkim92.blog_reactive.application.board.post.dto.CreatePostCommand
import kr.dhkim92.blog_reactive.common.error.BadRequestException
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

class Article(
    val id: Id<Article, UUID>? = null,
    title: String,
    contents: String,
    writerId: Id<Member, UUID>,
    categoryId: Id<Category, Long>,
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    isDeleted: Boolean = false
): BaseDomainEntity(
    createdAt = createdAt,
    updatedAt = updatedAt,
    isDeleted = isDeleted
) {

    var title: String = title
        protected set

    var contents: String = contents
        protected set

    var writerId: Id<Member, UUID> = writerId
        protected set

    var categoryId: Id<Category, Long> = categoryId
        protected set

    fun updateTitle(requester: Member, title: String) {
        checkPermission(requester)
        require(title.isNotBlank()) { throw BadRequestException(message="제목은 비어있을 수 없습니다.") }
        require(title.length < 255) { throw BadRequestException(message="제목은 255자 미만이어야 합니다.") }
        this.title = title
    }

    fun updateContents(requester: Member, contents: String) {
        checkPermission(requester)
        require(contents.isNotBlank()) { throw BadRequestException(message="내용은 비어있을 수 없습니다.") }
        this.contents = contents
    }

    fun changeCategory(requester: Member, category: Category) {
        checkPermission(requester)
        this.categoryId = category.id!!
    }

    fun increaseViewCount() {
        // TODO: 추후 구현, ArticleStats 테이블이 필요함
    }

    fun decreaseViewCount() {
        // TODO: 추후 구현, ArticleStats 테이블이 필요함
    }

    fun increaseCommentCount() {
        // TODO: 추후 구현, ArticleStats 테이블이 필요함
    }

    fun decreaseCommentCount() {
        // TODO: 추후 구현, ArticleStats 테이블이 필요함
    }

    fun increaseLikeCount() {
        // TODO: 추후 구현, ArticleStats 테이블이 필요함
    }

    fun decreaseLikeCount() {
        // TODO: 추후 구현, ArticleStats 테이블이 필요함
    }

    private fun checkPermission(requester: Member) {
        require(this.writerId == requester.id || requester.isAdmin() ) {
            throw ForbiddenException(message = "리소스 조작 권한 없음")
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Article) {
            other.id == this.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        fun create(
            requester: Member,
            category: Category,
            command: CreatePostCommand
        ): Article {
            require( requester.isAdmin() ) {
                throw ForbiddenException(message = "관리자만 가능한 작업입니다.")
            }
            require(command.title.isNotBlank()) { throw BadRequestException(message="제목은 비어있을 수 없습니다.") }
            require(command.title.length < 255) { throw BadRequestException(message="제목은 255자 미만이어야 합니다.") }
            require(command.content.isNotBlank()) { throw BadRequestException(message="내용은 비어있을 수 없습니다.") }
            require(category.id == command.categoryId) {
                throw BadRequestException(message="유효하지 않은 카테고리입니다.")
            }
            return Article(
                title = command.title,
                contents = command.content,
                writerId = requester.id!!,
                categoryId = category.id
            )
        }
    }
}