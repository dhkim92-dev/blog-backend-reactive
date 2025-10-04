package kr.dhkim92.blog_reactive.domain.member

import com.github.f4b6a3.uuid.UuidCreator
import kr.dhkim92.blog_reactive.common.error.ForbiddenException
import kr.dhkim92.blog_reactive.domain.BaseDomainEntity
import kr.dhkim92.blog_reactive.domain.Id
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class Member(
    id: Id<Member, UUID>? = null,
    nickname: String = "",
    email: String = "",
    password: String = "",
    role: MemberRole = MemberRole.MEMBER,
    isBlocked: Boolean = false,
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    _oauth2Infos: MutableSet<OAuth2Info> = mutableSetOf(),
    _refreshTokens: MutableSet<RefreshToken> = mutableSetOf(),
    isDeleted: Boolean = false
): BaseDomainEntity(createdAt, updatedAt, isDeleted) {

    var id = id
    protected set

    var nickname: String = nickname
    protected set

    var email: String = email
    protected set

    var password: String = password
    protected set

    var role: MemberRole = role
    protected set

    var isBlocked: Boolean = isBlocked
    protected set

    var _oauth2Infos = _oauth2Infos
    protected set

    var _refreshTokens = _refreshTokens
    protected set

    val oauth2Infos: Set<OAuth2Info>
        get() = _oauth2Infos.toSet()

    val refreshTokens: Set<RefreshToken>
        get() = _refreshTokens.toSet()

    fun linkOAuth2Account(requester: Member, info: OAuth2Info) {
        checkPermissions(requester)
        if (this._oauth2Infos.any { it.provider == info.provider }) {
            return
        }
        this._oauth2Infos.add(info)
    }

    fun unlinkOAuth2Account(requester: Member, info: OAuth2Info) {
        checkPermissions(requester)
        this._oauth2Infos.find{ it.userId == info.userId }?.let {
            it.markDeleted()
        }
    }

    fun addRefreshToken(requester: Member, token: RefreshToken) {
        checkPermissions(requester)
        require( token.expireAt > Instant.now()  ) {"만료된 토큰은 추가할 수 없습니다." }

        if ( this._refreshTokens.none { it == token } ) {
            // 전체 토큰들 중 만료된 토큰들을 markAsDeleted() 호출
            // 그리고 isDeleted 가 false인 것들만 고르고
            // 그것들이 5개 이상이라면 가장 최근 4개를 제외하고 전부 markAsDeleted 호출
            // 그 후 새로운 토큰 추가
            this._refreshTokens.filter { it.isDeleted || it.isExpired() }.forEach { it -> it.markDeleted() }
            if ( this._refreshTokens.count { !it.isDeleted } >= 5 ) {
                this._refreshTokens
                    .filter { !it.isDeleted }
                    .sortedByDescending { it.createdAt }
                    .drop(4)
                    .forEach { it -> it.markDeleted() }
            }
            this._refreshTokens.add(token)
        }
    }

    fun revokeRefreshToken(requester: Member, token: RefreshToken) {
        checkPermissions(requester)
        this._refreshTokens.find { it == token && !it.isDeleted }?.markDeleted()
    }

    fun changeNickname(requester: Member, nickname: String) {
        checkPermissions(requester)
        require( !nickname.isNullOrEmpty() ) { "닉네임은 비어있을 수 없습니다." }
        this.nickname = nickname
    }

    fun changePassword(member: Member, currentRawPassword: String, newPassword: String, passwordEncoder: PasswordEncoder) {
        checkPermissions(member)
        require(passwordEncoder.matches(currentRawPassword, this.password)) { throw IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.") }
        this.password = passwordEncoder.encode(password)
    }

    fun linkOAuth2Info(requester: Member, oAuth2Info: OAuth2Info) {
        require(this == requester) { throw ForbiddenException() }
        if (this._oauth2Infos.any { it.provider == oAuth2Info.provider }) {
            return
        }
        this._oauth2Infos.add(oAuth2Info)
    }

    fun unlinkOAuth2Info(requester: Member, oAuth2Info: OAuth2Info) {
        require(this == requester) { throw ForbiddenException() }
        this._oauth2Infos.find{ it.userId == oAuth2Info.userId }?.markDeleted()
    }

    fun isAdmin(): Boolean {
        return this.role == MemberRole.ADMIN
    }

    private fun checkPermissions(requester: Member) {
        if ( this != requester && !requester.isAdmin() ) {
            throw ForbiddenException()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Member) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
