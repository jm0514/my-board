package com.jm0514.myboard.auth.domain.oauthprovider;

import com.jm0514.myboard.global.exception.AuthException;
import com.jm0514.myboard.auth.domain.OauthAccessToken;
import com.jm0514.myboard.auth.domain.oauthuserinfo.KakaoUserInfo;
import com.jm0514.myboard.auth.domain.oauthuserinfo.OauthUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_AUTHORIZATION_CODE;
import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_OAUTH_SERVICE;

@Component
@Primary
public class KakaoOauthProvider implements OauthProvider{

    private static final String PROPERTIES_PATH = "${oauth2.provider.kakao.";
    private static final String PROVIDER_NAME = "kakao";

    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;
    protected final String tokenUri;
    protected final String userUri;

    public KakaoOauthProvider(
            @Value(PROPERTIES_PATH + "client-id}") final String clientId,
            @Value(PROPERTIES_PATH + "client-secret}") final String clientSecret,
            @Value(PROPERTIES_PATH + "redirect-uri}") final String redirectUri,
            @Value(PROPERTIES_PATH + "token-uri}") final String tokenUri,
            @Value(PROPERTIES_PATH + "user-info}") final String userUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
    }

    @Override
    public boolean is(final String name) {
        return PROVIDER_NAME.equals(name);
    }

    @Override
    public OauthUserInfo getUserInfo(final String code) {
        String accessToken = requestAccessToken(code);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> userInfoRequestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                userUri,
                HttpMethod.GET,
                userInfoRequestEntity,
                KakaoUserInfo.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new AuthException(INVALID_OAUTH_SERVICE);
    }

    private String requestAccessToken(final String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> accessTokenRequestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<OauthAccessToken> accessTokenResponse = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                accessTokenRequestEntity,
                OauthAccessToken.class
        );

        return Optional.ofNullable(accessTokenResponse.getBody())
                .orElseThrow(() -> new AuthException(INVALID_AUTHORIZATION_CODE))
                .getAccessToken();
    }
}
