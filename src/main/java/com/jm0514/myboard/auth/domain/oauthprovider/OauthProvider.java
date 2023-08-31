package com.jm0514.myboard.auth.domain.oauthprovider;

import com.jm0514.myboard.auth.domain.oauthuserinfo.OauthUserInfo;
import org.springframework.web.client.RestTemplate;

public interface OauthProvider {

    RestTemplate restTemplate = new RestTemplate();

    boolean is(String name);

    OauthUserInfo getUserInfo(String code);
}
