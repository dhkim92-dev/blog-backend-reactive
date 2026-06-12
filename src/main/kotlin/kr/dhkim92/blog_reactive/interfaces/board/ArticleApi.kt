package kr.dhkim92.blog_reactive.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.response.ListResponse
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.interfaces.board.dto.ArticleCommandResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.ArticleQueryResponse
import kr.dhkim92.blog_reactive.interfaces.board.dto.CreateArticleRequest
import kr.dhkim92.blog_reactive.interfaces.board.dto.UpdateArticleRequest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.UUID

@Tag(name = "Article API", description = "게시글 관련 API")
@RequestMapping("/api/v1")
interface ArticleApi {

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @PostMapping("/articles")
    fun postArticle(
        loginId: Id<Member, UUID>,
        request: CreateArticleRequest,
    ): Mono<ArticleCommandResponse>

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    @PutMapping("/articles/{articleId}")
    fun updateArticle(
        loginId: Id<Member, UUID>,
        articleId: UUID,
        request: UpdateArticleRequest,
    ): Mono<Void>

    @Operation(summary = "게시글 삭제", description = "기존 게시글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    @DeleteMapping("/articles/{articleId}")
    fun deleteArticle(
        loginId: Id<Member, UUID>,
        articleId: UUID,
    ): Mono<Void>

    @Operation(summary = "게시글 단건 조회", description = "특정 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
    @GetMapping("/articles/{articleId}")
    fun getArticle(
        articleId: UUID
    ): Mono<ArticleQueryResponse>

    @Operation(summary = "게시글 목록 조회", description = "카테고리별 게시글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    @GetMapping("/articles")
    fun getArticles(
        categoryId: Long?,
        cursor: UUID?,
        size: Int
    ): Mono<ListResponse<ArticleQueryResponse>>
}