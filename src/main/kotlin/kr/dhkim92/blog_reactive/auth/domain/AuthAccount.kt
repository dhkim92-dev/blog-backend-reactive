package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.time.Instant
import java.util.UUID

class AuthAccount(
    id: Id<AuthAccount, UUID>? = null,
    role: AuthRole = AuthRole.MEMBER,
    status: AuthAccountStatus = AuthAccountStatus.ACTIVE,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
    isDeleted: Boolean = false,
): BaseDomainEntity<AuthAccount, UUID>(id, createdAt, updatedAt, isDeleted) {

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