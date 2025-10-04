package kr.dhkim92.blog_reactive.port.persistence.member.r2dbc

import kr.dhkim92.blog_reactive.domain.member.r2dbc.MemberEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface R2DBCMemberRepository : R2dbcRepository<MemberEntity, UUID> {

    fun findByEmail(email: String): Mono<MemberEntity>

    fun findByNickname(nickname: String): Mono<MemberEntity>

    fun existsByEmail(email: String): Mono<Boolean>

    fun existsByNickname(nickname: String): Mono<Boolean>

    @Query("""
        SELECT m.* 
        FROM member m 
        LEFT JOIN oauth2_member o on m.id = o.member_id 
        WHERE o.user_id = :oAuth2UserId
    """)
    fun findByOAuth2UserId(oAuth2UserId: String): Mono<MemberEntity>
}