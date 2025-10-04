package kr.dhkim92.blog_reactive.domain.board.r2dbc

import kr.dhkim92.blog_reactive.application.board.dto.Writer
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.ArticleQueryModel
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

data class ArticleQueryRecord(
    var articleid: UUID,
    var articletitle: String,
    var articlecontents: String,
    var writerid: UUID,
    var writernickname: String,
    var categoryid: Long,
    var categoryname: String,
    var categorycount: Long, // Int -> Long으로 변경
    var articlecreatedat: LocalDateTime,
    var articleupdatedat: LocalDateTime
) {

    fun toArticleQueryModel(): ArticleQueryModel {
        return ArticleQueryModel(
            id = Id.of(Article::class, articleid),
            title = articletitle,
            content = articlecontents,
            writer = Writer(
                id = Id.of(Member::class, writerid),
                nickname = writernickname
            ),
            category = Category(
                id = Id(Category::class, categoryid),
                name = categoryname,
                count = categorycount.toInt()
            ),
            createdAt = articlecreatedat,
            updatedAt = articleupdatedat,
            viewCount = 0L,
            likeCount = 0L,
            commentCount = 0L
        )
    }
}