package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.global.exception.AuthException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_REFRESH_TOKEN_EXCEPTION;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column
    private String token;

    public RefreshToken(final Long memberId, final String token) {
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

    public Long getMemberId() {
        return memberId;
    }
}
