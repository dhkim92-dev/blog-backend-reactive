package kr.dhkim92.blog_reactive.member.domain

import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import kr.dhkim92.blog_reactive.common.error.BadRequestException
import kr.dhkim92.blog_reactive.common.jwt.AuthInfo
import java.time.Instant
import java.util.UUID

class Member(
    id: Id<Member, UUID>? = null,
    val authAccountId: Id<AuthInfo, UUID>,
    nickname: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),
    override var isDeleted: Boolean = false
): BaseDomainEntity<Member, UUID>(id) {

    var nickname: String = nickname
        private  set

    fun updateNickname(nickname: String?) {
        if ( nickname == null) return
        if ( isDeleted ) throw BadRequestException(message = "삭제된 회원의 정보를 수정할 수 없습니다")
        this.nickname = nickname
    }

    fun validate()  {
        if ( nickname.length > 20 ) {
            throw IllegalArgumentException("Nickname must be less than 20 characters")
        }
        if ( nickname.length < 3 ) {
            throw IllegalArgumentException("Nickname must be more than 2 characters")
        }
    }
}