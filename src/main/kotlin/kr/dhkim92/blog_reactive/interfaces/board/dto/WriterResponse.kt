package kr.dhkim92.blog_reactive.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.common.response.BaseResponse
import java.util.UUID

@Schema(description = "작성자 응답")
data class WriterResponse(
    @Schema(description = "작성자 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val id: UUID,
    @Schema(description = "작성자 닉네임", example = "dhkim92")
    val nickname: String
): BaseResponse() {

    companion object {

        fun from(writer: Writer): WriterResponse {
            return WriterResponse(
                id = writer.id.value,
                nickname = writer.nickname
            )
        }
    }
}