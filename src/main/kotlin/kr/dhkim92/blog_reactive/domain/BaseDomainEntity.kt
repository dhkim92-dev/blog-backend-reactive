package kr.dhkim92.blog_reactive.domain

import java.time.LocalDateTime

abstract class BaseDomainEntity(
    open var createdAt: LocalDateTime = LocalDateTime.now(),
    open var updatedAt: LocalDateTime = LocalDateTime.now(),
    open var isDeleted: Boolean = false
) {

    open fun markDeleted() {
        isDeleted = true
    }
}