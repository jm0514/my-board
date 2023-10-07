package com.jm0514.myboard.auth.domain.oauthprovider;

import com.jm0514.myboard.global.exception.AuthException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_OAUTH_SERVICE;

@Component
public class OauthProviders {

    private final List<OauthProvider> providers;

    public OauthProviders(final List<OauthProvider> providers) {
        this.providers = providers;
    }

    public OauthProvider mapping(final String providerName) {
        return providers.stream()
                .filter(provider -> provider.is(providerName))
                .findFirst()
                .orElseThrow(() -> new AuthException(INVALID_OAUTH_SERVICE));
    }
}
