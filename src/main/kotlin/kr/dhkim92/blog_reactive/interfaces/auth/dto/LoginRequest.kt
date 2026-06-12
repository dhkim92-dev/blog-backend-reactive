package kr.dhkim92.blog_reactive.interfaces.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import kr.dhkim92.blog_reactive.auth.application.dto.LoginCommand

@Schema(description = "Login Request")
class LoginRequest(
    @Schema(description = "Email", example = "test@dohoon-kim.kr")
    @field: Email(message = "Invalid email format")
    val email: String,
    @Schema(description = "Password", example = "test1234")
    @field: NotEmpty(message = "Password must not be empty")
    val password: String
) {

    fun toCommand(): LoginCommand {
        return LoginCommand(
            email = email,
            password = password
        )
    }
}