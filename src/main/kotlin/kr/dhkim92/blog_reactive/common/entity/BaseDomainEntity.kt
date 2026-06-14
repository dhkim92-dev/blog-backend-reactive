package kr.dhkim92.blog_reactive.common.entity

import java.time.Instant


abstract class BaseDomainEntity<C : Any, T: Any> (
    id: Id<C, T>? = null,
    open var createdAt: Instant = Instant.now(),
    open var updatedAt: Instant = Instant.now(),
    open var isDeleted: Boolean = false
) {

    var id: Id<C, T>? = id

    protected set

    val identifier: Id<C, T>
        get() = requireNotNull(id) {
            "${this::class.simpleName} id has not been assigned yet"
        }

    open fun markNotDeleted() {
        isDeleted = false
    }

    open fun markDeleted() {
        isDeleted = true
    }
}