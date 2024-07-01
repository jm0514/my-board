package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.auth.domain.repository.RefreshTokenRedisRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class RefreshTokenServiceTest extends IntegrationTestSupport {

    public static final String REFRESH_TOKEN = "refreshToken";

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRedisRepository.deleteAll();
        memberRepository.deleteAllInBatch();
    }

    public static final String TOKEN = "token";
    public static final String MEMBER_ID = "1";

    @DisplayName("리프레시 토큰을 저장할 수 있다.")
    @Test
    void savedToken() {
        // given
        Member createdMember = getMember();
        memberRepository.save(createdMember);

        // when then
        refreshTokenService.saveToken(MEMBER_ID, TOKEN);
    }

    @DisplayName("저장된 리프레시 토큰과 요청된 리프레시 토큰이 같은지 비교한다.")
    @Test
    void match() {
        // given
        Member createdMember = getMember();
        memberRepository.save(createdMember);

        RefreshToken refreshToken = new RefreshToken(createdMember.getId().toString(), REFRESH_TOKEN);
        refreshTokenRedisRepository.save(refreshToken);

        given(jwtProvider.validateRefreshToken(any())).willReturn(true);

        // when then
        refreshTokenService.match(MEMBER_ID, REFRESH_TOKEN);

    }

    private Member getMember() {
        return Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .profileImageUrl("Url")
                .roleType(RoleType.USER)
                .build();
    }
}