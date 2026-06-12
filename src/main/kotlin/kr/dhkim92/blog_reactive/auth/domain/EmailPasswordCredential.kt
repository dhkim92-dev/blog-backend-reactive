package kr.dhkim92.blog_reactive.auth.domain

import kr.dhkim92.blog_reactive.common.data.Email
import kr.dhkim92.blog_reactive.common.entity.BaseDomainEntity
import kr.dhkim92.blog_reactive.common.entity.Id
import java.util.UUID

class EmailPasswordCredential(
    id: Id<EmailPasswordCredential, UUID>? = null,
    authAccountId: Id<AuthAccount, UUID>,
    email: Email,
    password: String,
): BaseDomainEntity<EmailPasswordCredential, UUID>(id) {

    var authAccountId = authAccountId
    private set

    var email = email
    private set

    var password = password
    private set

    fun changePassword(
        newPassword: String,
    ) {
        require(newPassword.isNotBlank() ) {
            "New password must not be blank"
        }
        this.password = newPassword
    }
}