package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.global.exception.AuthException;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_REFRESH_TOKEN_EXCEPTION;

@RedisHash(value = "refreshToken", timeToLive = 1209600000)
public class RefreshToken {

    @Id
    private String token;

    @Indexed
    private String memberId;

    public RefreshToken(final String memberId,final String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public void validateSameToken(final String token) {
        if (!this.token.equals(token)) {
            throw new AuthException(INVALID_REFRESH_TOKEN_EXCEPTION);
        }
    }

    public String getToken() {
        return token;
    }

    public String getMemberId() {
        return memberId;
    }
}
