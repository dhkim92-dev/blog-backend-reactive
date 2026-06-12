package kr.dhkim92.blog_reactive.common.data

import java.util.regex.Pattern

class Email(
    val value: String
) {

    init {
        if (!regex.matcher(value).matches()) {
            throw IllegalArgumentException("Invalid email address")
        }
    }

    companion object {
        private val regex = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
    }
}