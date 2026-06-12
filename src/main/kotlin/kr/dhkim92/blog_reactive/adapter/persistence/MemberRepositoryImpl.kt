package kr.dhkim92.blog_reactive.adapter.persistence

import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import kr.dhkim92.blog_reactive.domain.member.OAuth2Info
import kr.dhkim92.blog_reactive.auth.domain.RefreshToken
import kr.dhkim92.blog_reactive.domain.member.mapper.MemberMapper
import kr.dhkim92.blog_reactive.domain.member.mapper.OAuth2InfoMapper
import kr.dhkim92.blog_reactive.domain.member.mapper.RefreshTokenMapper
import kr.dhkim92.blog_reactive.domain.member.r2dbc.MemberEntity
import kr.dhkim92.blog_reactive.domain.member.r2dbc.OAuth2InfoEntity
import kr.dhkim92.blog_reactive.domain.member.r2dbc.RefreshTokenEntity
import kr.dhkim92.blog_reactive.port.persistence.member.MemberRepository
import kr.dhkim92.blog_reactive.port.persistence.member.r2dbc.R2DBCMemberRepository
import kr.dhkim92.blog_reactive.port.persistence.member.r2dbc.R2DBCOAuth2InfoRepository
import kr.dhkim92.blog_reactive.port.persistence.member.r2dbc.R2DBCRefreshTokenRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class MemberRepositoryImpl(
    private val r2dbcMemberRepository: R2DBCMemberRepository,
    private val r2dbcRefreshTokenRepository: R2DBCRefreshTokenRepository,
    private val r2dbcOAuth2InfoRepository: R2DBCOAuth2InfoRepository
) : MemberRepository {

    override fun save(member: Member): Mono<Member> {
        // 1. MemberEntity로 변환 후 저장
        val memberEntity = MemberMapper.toEntity(member)
        return r2dbcMemberRepository.save(memberEntity)
            .flatMap { savedMemberEntity ->
                val memberId = savedMemberEntity.id!!

                // 2. isDeleted가 true로 설정된 refreshTokens 모아서 한 번에 삭제
                val refreshTokenIds = member.refreshTokens
                    .filter { it.isDeleted }
                    .mapNotNull { it.id?.value }

                val refreshTokenDeleteOp = if (refreshTokenIds.isNotEmpty()) {
                    r2dbcRefreshTokenRepository.deleteAllById(refreshTokenIds)
                } else {
                    Mono.empty()
                }

                // 3. isDeleted가 true로 설정된 oauth2Infos 모아서 한 번에 삭제
                val oauth2InfoIds = member.oauth2Infos
                    .filter { it.isDeleted }
                    .mapNotNull { it.id?.value }

                val oauth2InfoDeleteOp = if (oauth2InfoIds.isNotEmpty()) {
                    r2dbcOAuth2InfoRepository.deleteAllById(oauth2InfoIds)
                } else {
                    Mono.empty()
                }

                // 모든 삭제 작업 완료 대기
                Mono.`when`(refreshTokenDeleteOp, oauth2InfoDeleteOp)
                    .then(Mono.defer {
                        // 4. 삭제 대상이 아닌 모든 refreshTokens 저장 (새로운 것과 업데이트된 것 모두)
                        val refreshTokenSaveOperations = member.refreshTokens
                            .filter { !it.isDeleted } // 삭제 대상이 아닌 모든 토큰 선택
                            .map { refreshToken ->
                                val entity = RefreshTokenMapper.toEntity(refreshToken)
                                r2dbcRefreshTokenRepository.save(entity)
                            }

                        // 5. 삭제 대상이 아닌 모든 oauth2Infos 저장 (새로운 것과 업데이트된 것 모두)
                        val oauth2InfoSaveOperations = member.oauth2Infos
                            .filter { !it.isDeleted } // 삭제 대상이 아닌 모든 OAuth2 정보 선택
                            .map { oauth2Info ->
                                val entity = OAuth2InfoMapper.toEntity(oauth2Info)
                                r2dbcOAuth2InfoRepository.save(entity)
                            }

                        // 모든 저장 작업 완료 대기
                        Mono.`when`(refreshTokenSaveOperations + oauth2InfoSaveOperations)
                    })
                    // 6. 위 과정이 다 끝난 이후 findById를 호출하여 Member를 다시 조회하여 반환
                    .then(findById(Id.of(Member::class, memberId)))
            }
    }

    override fun delete(member: Member) {
        val entity = MemberMapper.toEntity(member)
        r2dbcMemberRepository.delete(entity).subscribe()
    }

    private fun loadRefreshTokens(memberId: UUID): Flux<RefreshTokenEntity> {
        return r2dbcRefreshTokenRepository.findByMemberId(memberId)
            .filter { !it.isDeleted }

    }

    private fun loadOAuth2Infos(memberId: UUID): Flux<OAuth2InfoEntity> {
        return r2dbcOAuth2InfoRepository.findByMemberId(memberId)
    }

    override fun findById(id: Id<Member, UUID>): Mono<Member> {
        return r2dbcMemberRepository.findById(id.value)
            .flatMap { memberEntity ->
                Mono.zip(
                    loadRefreshTokens(memberEntity.id!!).collectList(),
                    loadOAuth2Infos(memberEntity.id!!).collectList()
                ).map { tuple ->
                    val refreshTokens = tuple.t1.toSet()
                    val oauth2Infos = tuple.t2.toSet()
                    MemberMapper.toDomain(memberEntity, refreshTokens, oauth2Infos)
                }
            }
    }

    override fun findByEmail(email: String): Mono<Member> {
        return r2dbcMemberRepository.findByEmail(email)
            .flatMap { memberEntity ->
                Mono.zip(
                    loadRefreshTokens(memberEntity.id!!).collectList(),
                    loadOAuth2Infos(memberEntity.id!!).collectList()
                ).map { tuple ->
                    val refreshTokens = tuple.t1.toSet()
                    val oauth2Infos = tuple.t2.toSet()
                    MemberMapper.toDomain(memberEntity, refreshTokens, oauth2Infos)
                }
            }
    }

    override fun findByNickname(nickname: String): Mono<Member> {
        return r2dbcMemberRepository.findByNickname(nickname)
            .flatMap { memberEntity ->
                Mono.zip(
                    loadRefreshTokens(memberEntity.id!!).collectList(),
                    loadOAuth2Infos(memberEntity.id!!).collectList()
                ).map { tuple ->
                    val refreshTokens = tuple.t1.toSet()
                    val oauth2Infos = tuple.t2.toSet()
                    MemberMapper.toDomain(memberEntity, refreshTokens, oauth2Infos)
                }
            }
    }

    override fun existsByEmail(email: String): Mono<Boolean> {
        return r2dbcMemberRepository.existsByEmail(email)
    }

    override fun existsByNickname(nickname: String): Mono<Boolean> {
        return r2dbcMemberRepository.existsByNickname(nickname)
    }

    override fun findByOAuth2UserId(userId: String): Mono<Member> {
        return r2dbcMemberRepository.findByOAuth2UserId(userId)
            .flatMap { memberEntity ->
                Mono.zip(
                    loadRefreshTokens(memberEntity.id!!).collectList(),
                    loadOAuth2Infos(memberEntity.id!!).collectList()
                ).map { tuple ->
                    val refreshTokens = tuple.t1.toSet()
                    val oauth2Infos = tuple.t2.toSet()
                    MemberMapper.toDomain(memberEntity, refreshTokens, oauth2Infos)
                }
            }
    }
}