package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.application.board.comment.dto.UpdateCommentCommand
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Comment
import org.hibernate.validator.constraints.Length
import java.util.UUID

@Schema(description = "댓글 수정 요청")
data class UpdateCommentRequest(
    @field: NotEmpty(message = "댓글 내용을 입력해주세요.")
    @field: Length(max = 500, message = "댓글 내용은 최대 500자까지 입력 가능합니다.")
    @field:Schema(description = "댓글 내용", example = "수정된 댓글 내용")
    val content: String
) {

    fun toCommand(commentId: UUID): UpdateCommentCommand {
        return UpdateCommentCommand(
            commentId = Id.of(Comment::class, commentId),
            content = content
        )
    }
}