package kr.dhkim92.blog_reactive.port.persistence.member

import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import reactor.core.publisher.Mono
import java.util.UUID

interface MemberRepository {

    fun save(member: Member): Mono<Member>

    fun delete(member: Member)

    fun findById(id: Id<Member, UUID>): Mono<Member>

    // 이메일로 회원 찾기
    fun findByEmail(email: String): Mono<Member>

    // 닉네임으로 회원 찾기
    fun findByNickname(nickname: String): Mono<Member>

    // 이메일 존재 여부 확인
    fun existsByEmail(email: String): Mono<Boolean>

    // 닉네임 존재 여부 확인
    fun existsByNickname(nickname: String): Mono<Boolean>

    // OAuth2 소셜 로그인 회원 찾기
    fun findByOAuth2UserId(userId: String): Mono<Member>
}