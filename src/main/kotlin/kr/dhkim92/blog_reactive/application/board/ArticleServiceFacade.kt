package kr.dhkim92.blog_reactive.application.board

import kr.dhkim92.blog_reactive.application.board.post.dto.CreatePostCommand
import kr.dhkim92.blog_reactive.application.board.post.dto.UpdatePostCommand
import kr.dhkim92.blog_reactive.application.board.post.usecases.CreatePostUseCase
import kr.dhkim92.blog_reactive.application.board.post.usecases.DeletePostUseCase
import kr.dhkim92.blog_reactive.application.board.post.usecases.QueryPostUseCase
import kr.dhkim92.blog_reactive.application.board.post.usecases.UpdatePostUseCase
import org.springframework.stereotype.Service
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.board.Category
import kr.dhkim92.blog_reactive.domain.member.Member
import java.util.UUID

@Service
class ArticleServiceFacade(
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val queryPostUseCase: QueryPostUseCase
) {

    fun create(loginId: Id<Member, UUID>, command: CreatePostCommand) =
        createPostUseCase.create(loginId, command)

    fun update(loginId: Id<Member, UUID>, command: UpdatePostCommand) =
        updatePostUseCase.update(loginId, command)

    fun delete(loginId: Id<Member, UUID>, postId: Id<Article, UUID>) =
        deletePostUseCase.delete(loginId, postId)

    fun getPost(id: Id<Article, UUID>) = queryPostUseCase.getPost(id)

    fun getPostsByCategoryIdAndCursorIdPagination(
        categoryId: Id<Category, Long>?,
        cursorId: Id<Article, UUID>?,
        size: Int
    ) = queryPostUseCase.getPostsByCategoryIdAndCursorIdPagination(
        categoryId = categoryId,
        cursorId = cursorId,
        size = size
    )
}