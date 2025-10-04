package kr.dhkim92.blog_reactive.domain.board

import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

class ArticleQueryModel(
    val id: Id<Article, UUID>,
    val title: String,
    val content: String,
    val writer: Writer,
    val category: Category,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long
) {

}