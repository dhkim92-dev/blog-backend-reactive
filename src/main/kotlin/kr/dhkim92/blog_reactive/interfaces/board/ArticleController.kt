package kr.dhkim92.blog_reactive.interfaces.board

import jakarta.validation.Valid
import kr.dhkim92.blog_reactive.application.board.ArticleServiceFacade
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.annotations.Login
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.board.dto.ArticleCommandResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.ArticleQueryResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateArticleRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.UpdateArticleRequest
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class ArticleController(
    private val articleServiceFacade: ArticleServiceFacade
): ArticleApi {

    @Envelope(status = CREATED, message = "게시글 작성 성공", code = "A001")
    override fun postArticle(
        @Login loginId: Id<Member, UUID>,
        @RequestBody @Valid request: CreateArticleRequest
    ): Mono<ArticleCommandResponse> {
        return articleServiceFacade.create(loginId, request.toCommand())
            .map { ArticleCommandResponse.from(it) }
    }

    @Envelope(status = OK, message = "게시글 수정 성공", code = "A002")
    override fun updateArticle(
        @Login loginId: Id<Member, UUID>,
        @PathVariable articleId: UUID,
        @RequestBody @Valid request: UpdateArticleRequest)
    : Mono<Void> {
        return articleServiceFacade.update(
            loginId,
            request.toCommand(articleId)
        )
    }

    @Envelope(status = NO_CONTENT, message = "게시글 삭제 성공", code = "A003")
    override fun deleteArticle(
        @Login loginId: Id<Member, UUID>,
        @PathVariable articleId: UUID)
    : Mono<Void> {
        return articleServiceFacade.delete(loginId, Id.of(Article::class, articleId))
    }


    @Envelope(status = OK, message = "게시글 조회 성공", code = "A004")
    override fun getArticle(
        @PathVariable articleId: UUID
    ): Mono<ArticleQueryResponse> {
        return articleServiceFacade.getPost(
            id = Id.of(Article::class, articleId)
        ).map {
            ArticleQueryResponse.from(it)
        }
    }

    @Envelope(status = OK, message = "게시글 목록 조회 성공", code = "A005")
    override fun getArticles(
        @RequestParam(required=false) categoryId: Long?,
        @RequestParam(required=false) cursor: UUID?,
        @RequestParam(defaultValue = "20")size: Int
    ): Mono<ListResponse<ArticleQueryResponse>> {
        return articleServiceFacade.getPostsByCategoryIdAndCursorIdPagination(
            categoryId = categoryId?.let { Id.of(Category::class, it) },
            cursorId = cursor?.let { Id.of(Article::class, it) },
            size = size + 1
        ).map { ArticleQueryResponse.from(it) }
        .collectList()
        .flatMap { items ->
            ListResponse.ofAsync(
                size = size,
                items = items,
                extractors = buildMap {
                    put("categoryId") { categoryId?.toString() }
                    put("cursor") { it.id }
                }
            )
        }
    }
}