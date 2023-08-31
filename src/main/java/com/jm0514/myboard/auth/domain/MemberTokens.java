package com.jm0514.myboard.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberTokens {

    private final String refreshToken;
    private final String accessToken;
}
