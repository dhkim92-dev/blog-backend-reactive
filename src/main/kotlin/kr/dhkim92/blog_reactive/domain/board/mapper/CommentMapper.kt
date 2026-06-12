package kr.dhkim92.blog_reactive.domain.board.mapper

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import kr.dhkim92.blog_reactive.domain.board.r2dbc.CommentEntity
import kr.dhkim92.blog_reactive.domain.member.Member

object CommentMapper {

    fun toEntity(comment: Comment): CommentEntity {
        return CommentEntity(
            id = comment.id?.value,
            articleId = comment.articleId.value,
            parentId = comment.parentId?.value,
            memberId = comment.writerId.value,
            content = comment.content,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt,
            isDeleted = comment.isDeleted,
            replyCount = comment.replyCount,
            depth = comment.depth
        )
    }

    fun toDomain(entity: CommentEntity): Comment {
        return Comment(
            id = entity.id?.let { Id.of(Comment::class, it) },
            articleId = Id.of(Article::class, entity.articleId),
            parentId = entity.parentId?.let { Id.of(Comment::class, it) },
            writerId = Id.of(Member::class, entity.memberId),
            content = entity.content,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted,
            replyCount = entity.replyCount,
            depth = entity.depth
        )
    }
}