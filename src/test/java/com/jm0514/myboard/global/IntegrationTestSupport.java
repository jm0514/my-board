package com.jm0514.myboard.global;

import com.jm0514.myboard.auth.domain.JwtProvider;
import com.jm0514.myboard.auth.domain.RefreshTokenService;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProvider;
import com.jm0514.myboard.auth.domain.oauthprovider.OauthProviders;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected RefreshTokenService refreshTokenService;

    @MockBean
    protected OauthProviders oauthProviders;

    @MockBean
    protected OauthProvider oauthProvider;
}
