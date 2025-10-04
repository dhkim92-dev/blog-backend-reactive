package kr.dhkim92.blog_reactive.domain

import kotlin.reflect.KClass

class Id<R : Any, V : Any>(
    val reference: KClass<R>,
    val value: V
) {

    companion object {

        fun <R : Any, V : Any> of(reference: KClass<R>, value: V): Id<R, V> {
            return Id(reference, value)
        }

        // 자바 Class를 사용하는 기존 코드와의 호환성을 위한 함수
        fun <R : Any, V : Any> of(reference: Class<R>, value: V): Id<R, V> {
            return Id(reference.kotlin, value)
        }
    }

    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is Id<*, *>
                && value == other.value)
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}