package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.util.UUID

class AuthMember(
    id: Id<AuthMember, UUID>? = null,
    val nickname: String,
): BaseDomainEntity<AuthMember, UUID>(id) {

}