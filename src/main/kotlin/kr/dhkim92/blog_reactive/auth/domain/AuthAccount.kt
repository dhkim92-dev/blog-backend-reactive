package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.util.UUID

class AuthAccount(
    id: Id<AuthAccount, UUID>? = null,
    memberId: Id<AuthMember, UUID>,
    role: AuthRole = AuthRole.MEMBER,
    status: AuthAccountStatus = AuthAccountStatus.ACTIVE
): BaseDomainEntity<AuthAccount, UUID>(id) {

    var memberId = memberId
    private set

    var role = role
    private set

    var status = status
    private set

    fun markAsBlock() {
        status = AuthAccountStatus.BLOCKED
    }

    fun markAsActive() {
        status = AuthAccountStatus.ACTIVE
    }

    fun isActive(): Boolean {
        return id != null && status == AuthAccountStatus.ACTIVE && !isDeleted
    }

    fun changeRole(role: AuthRole) {
        if ( this.role != role ) {
            this.role = role
        }
    }
}