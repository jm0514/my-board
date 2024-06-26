package com.jm0514.myboard.auth.service;

import com.jm0514.myboard.auth.domain.JwtProvider;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.domain.RefreshToken;
import com.jm0514.myboard.auth.domain.RefreshTokenService;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProviders;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProvider;
import com.jm0514.myboard.auth.domain.oauthuserinfo.OauthUserInfo;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OauthProviders oauthProviders;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public MemberTokens login(final String providerName, final String code) {
        OauthProvider provider = oauthProviders.mapping(providerName);
        OauthUserInfo oauthUserInfo = provider.getUserInfo(code);
        Member member = findAndCreateMember(
                oauthUserInfo.getLoginAccountId(),
                oauthUserInfo.getNickname(),
                oauthUserInfo.getImageUrl()
        );
        AuthInfo authInfo = new AuthInfo(member.getId(), member.getRoleType().getValue());
        MemberTokens memberTokens = jwtProvider.generateLoginToken(authInfo);

        refreshTokenService.deleteToken(member.getId().toString());

        RefreshToken savedRefreshToken = new RefreshToken(member.getId().toString(), memberTokens.getRefreshToken());
        refreshTokenService.saveToken(savedRefreshToken.getMemberId(), savedRefreshToken.getToken());

        return memberTokens;
    }

    private Member findAndCreateMember(
            final String loginAccountId,
            final String nickname,
            final String profileImageUrl
    ) {
        return memberRepository.findByLoginAccountId(loginAccountId)
                .orElseGet(() -> memberRepository.save(Member.builder()
                                .loginAccountId(loginAccountId)
                                .name(nickname)
                                .profileImageUrl(profileImageUrl)
                                .roleType(RoleType.USER)
                        .build()));
    }
}
