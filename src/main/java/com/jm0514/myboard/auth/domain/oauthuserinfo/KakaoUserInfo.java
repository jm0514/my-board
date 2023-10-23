package com.jm0514.myboard.auth.domain.oauthuserinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Generated
public class KakaoUserInfo implements OauthUserInfo{

    @JsonProperty("id")
    private String loginAccountId;

    @JsonProperty("properties")
    private Properties properties;

    @Override
    public String getLoginAccountId() {
        return loginAccountId;
    }

    @Override
    public String getNickname() {
        return properties.name;
    }

    @Override
    public String getImageUrl() {
        return properties.image;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class Properties {

        @JsonProperty("nickname")
        private String name;

        @JsonProperty("profile_image")
        private String image;
    }
}
