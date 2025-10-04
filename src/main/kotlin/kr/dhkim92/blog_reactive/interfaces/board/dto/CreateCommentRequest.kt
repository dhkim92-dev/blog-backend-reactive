package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.comment.dto.CreateCommentCommand
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import java.util.UUID

@Schema(description = "댓글 생성 요청")
data class CreateCommentRequest(
    @field:Schema(description = "댓글 내용", example = "댓글 내용입니다.", required = true)
    val content: String
) {

    fun toCommand(postId: UUID) = CreateCommentCommand(
        postId = Id.of(Article::class, postId),
        content = content
    )
}