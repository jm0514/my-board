package com.jm0514.myboard.auth.service;

import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProvider;
import com.jm0514.myboard.auth.domain.oauthuserinfo.OauthUserInfo;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

class AuthServiceTest extends IntegrationTestSupport {

    public static final String KAKAO = "kakao";
    public static final String CODE = "code";
    private final static String REFRESH_TOKEN = "refreshToken";
    private final static String ACCESS_TOKEN = "Bearer accessToken";

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("로그인을 할 수 있습니다.")
    @Test
    void login(){
        // given
        Member createdMember = Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .profileImageUrl("Url")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(createdMember);

        MemberTokens memberTokens = new MemberTokens(REFRESH_TOKEN, ACCESS_TOKEN);

        OauthUserInfo oauthUserInfo = Mockito.mock(OauthUserInfo.class);
        given(oauthUserInfo.getLoginAccountId()).willReturn("1234");
        given(oauthUserInfo.getNickname()).willReturn("정민");
        given(oauthUserInfo.getImageUrl()).willReturn("Url");

        OauthProvider provider = Mockito.mock(OauthProvider.class);

        given(oauthProviders.mapping(KAKAO)).willReturn(provider);

        given(provider.getUserInfo(CODE)).willReturn(oauthUserInfo);

        given(oauthProvider.getUserInfo(CODE)).willReturn(oauthUserInfo);

        given(jwtProvider.generateLoginToken(any())).willReturn(memberTokens);

        doNothing().when(refreshTokenService).saveToken(any(), any());

        // when
        MemberTokens result = authService.login(KAKAO, CODE);

        // then
        assertThat(result).isEqualTo(memberTokens);
    }
}