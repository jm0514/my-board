package com.jm0514.myboard.auth.domain.oauthuserinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GoogleUserInfo implements OauthUserInfo{

    @JsonProperty("id")
    private String loginAccountId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("picture")
    private String picture;

    @Override
    public String getLoginAccountId() {
        return loginAccountId;
    }

    @Override
    public String getNickname() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return picture;
    }

}
