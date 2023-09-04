package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.auth.exception.InvalidRefreshTokenException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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
            throw new InvalidRefreshTokenException();
        }
    }
}
