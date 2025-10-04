package kr.dhkim92.blog_reactive.application.member

import kr.dhkim92.blog_reactive.application.auth.oauth2.dto.OAuth2UserInfo
import kr.dhkim92.blog_reactive.application.member.dto.CreateMemberCommand
import kr.dhkim92.blog_reactive.application.member.dto.UpdateMemberCommand
import kr.dhkim92.blog_reactive.application.member.usecases.CreateMemberUseCase
import kr.dhkim92.blog_reactive.application.member.usecases.DeleteMemberUseCase
import kr.dhkim92.blog_reactive.application.member.usecases.QueryMemberUseCase
import kr.dhkim92.blog_reactive.application.member.usecases.UpdateMemberUseCase
import kr.dhkim92.blog_reactive.domain.Id
import kr.dhkim92.blog_reactive.domain.member.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberServiceFacade(
    private val createMemberUseCase: CreateMemberUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val queryMemberUseCase: QueryMemberUseCase
) {

    fun createMember(command: CreateMemberCommand) = createMemberUseCase.create(command)

    fun createMemberByOAuth2UserInfo(userInfo: OAuth2UserInfo) =
        createMemberUseCase.createByOAuth2UserInfo(userInfo)

    fun updateMember(loginId: Id<Member, UUID>, memberId: Id<Member, UUID>, command: UpdateMemberCommand) =
        updateMemberUseCase.update(loginId, memberId, command)

    fun deleteMember(loginId: Id<Member, UUID>, memberId: Id<Member, UUID>) =
        deleteMemberUseCase.delete(loginId, memberId)

    fun getMember(memberId: Id<Member, UUID>) = queryMemberUseCase.getMember(memberId)

    fun getMemberByOAuth2UserInfo(userInfo: OAuth2UserInfo) =
        queryMemberUseCase.getMemberByOAuth2UserInfo(userInfo)
}