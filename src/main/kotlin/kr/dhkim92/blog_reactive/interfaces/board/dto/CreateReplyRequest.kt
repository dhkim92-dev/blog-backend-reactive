package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateReplyCommand
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Comment
import java.util.UUID

@Schema(description = "대댓글 생성 요청")
data class CreateReplyRequest(
    @field:Schema(description = "대댓글 내용", example = "대댓글 내용입니다.", required = true)
    val content: String
) {

    fun toCommand(
        commentId: UUID
    ): CreateReplyCommand {
        return CreateReplyCommand(
            parentId = Id.of(Comment::class, commentId),
            content = content
        )
    }
}