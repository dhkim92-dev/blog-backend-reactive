package kr.dhkim92.blog_reactive.domain.board.r2dbc

import java.time.LocalDateTime
import java.util.UUID

data class CommentQueryRecord(
    val id: UUID,
    val parentid: UUID?,
    val articleid: UUID,
    val writerid: UUID,
    val writername: String,
    val content: String,
    val replycount: Int,
    val createdat: LocalDateTime,
    val updatedat: LocalDateTime
) {

}