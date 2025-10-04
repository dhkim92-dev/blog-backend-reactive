package kr.dhkim92.blog_reactive.application.board.post.usecases

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.board.Article
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface DeletePostUseCase {

    fun delete(loginId: Id<Member, UUID>, postId: Id<Article, UUID>): Mono<Void>
}