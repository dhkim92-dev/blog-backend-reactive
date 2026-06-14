package kr.dhkim92.blog_reactive.common.annotations

import io.swagger.v3.oas.annotations.Parameter

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@Parameter(hidden = true)
annotation class AuthId()
