package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.auth.domain.exceptions.InvalidPasswordException
import java.util.regex.Pattern

class Password(
    val value: String
) {

    init {
        validate();
    }

    fun validate() {
        if ( !PASSWORD_PATTERN.matcher(value).matches() ) {
            throw InvalidPasswordException()
        }
    }

    companion object {
        private val PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()_\\-+=\\[\\]{};:'\",.<>/?\\\\|`~])[A-Za-z0-9!@#\$%^&*()_\\-+=\\[\\]{};:'\",.<>/?\\\\|`~]{8,30}$"
        )
    }
}