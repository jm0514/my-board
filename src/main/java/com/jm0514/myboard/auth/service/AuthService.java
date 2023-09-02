package com.jm0514.myboard.auth.service;

import com.jm0514.myboard.auth.domain.JwtProvider;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.domain.RefreshToken;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProviders;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProvider;
import com.jm0514.myboard.auth.domain.oauthuserinfo.OauthUserInfo;
import com.jm0514.myboard.auth.repository.RefreshTokenRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OauthProviders oauthProviders;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberTokens login(final String providerName, final String code) {
        OauthProvider provider = oauthProviders.mapping(providerName);
        OauthUserInfo oauthUserInfo = provider.getUserInfo(code);
        Member member = findAndCreateMember(
                oauthUserInfo.getLoginAccountId(),
                oauthUserInfo.getNickname(),
                oauthUserInfo.getImageUrl()
        );
        MemberTokens memberTokens = jwtProvider.generateLoginToken(member.getId().toString());
        RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(), member.getId());
        refreshTokenRepository.save(savedRefreshToken);
        return memberTokens;
    }

    private Member findAndCreateMember(final String loginAccountId, final String nickname, final String profileImageUrl) {
        return memberRepository.findByLoginAccountId(loginAccountId)
                .orElseGet(() -> memberRepository.save(new Member(loginAccountId, nickname, profileImageUrl)));
    }
}
