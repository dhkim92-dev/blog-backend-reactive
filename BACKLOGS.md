# BACKLOGS.md

## TASK1 - JWT Reissue 
**STATUS** Ready
**BACKGROUND**

JWT 액세스 토큰 재발급을 위한 ReissueJWTService 구현 요구
- refreshToken을 디코드하면 subject가 있습니다. 이는 AuthMember의 id입니다.
- 토큰으로 AuthSession이 존재하는지 확인하고 존재하지 않으면 UnathorizedException 발생할 것
- Access Token 발행 시에는 AuthAccount와 AuthMember를 조회해야 생성 가능함
- Access Token 발급 시 인증 계정의 존재 여부와 활성화 여부가 체크되어야 함
- ReissueJWTService만 건드리십시오. 
- 현재 발급된 refreshToken의 만료 시간이 하루 내라면 rotate를 실행 시킨 뒤 refreshToken을 저장하도록 하세요.
- 만약 refreshToken이 만료된 경우, refreshToken을 삭제합니다.
- 다른 파일을 건드려야하면 사용자에게 먼저 요청을 한 뒤 허가를 받으세요
**OBJECTS**
target package. kr.dhkim992.blog_reactive.auth
- application.ReissueJWTService
- application.JWTService
- application.port.*
- domain.AuthSession
- domain.AuthAccount
- domain.AuthMember