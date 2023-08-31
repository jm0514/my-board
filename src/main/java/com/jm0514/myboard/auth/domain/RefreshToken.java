package com.jm0514.myboard.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String token;

    @Column(nullable = false, unique = true)
    private Long memberId;

    public RefreshToken(final String token, final Long memberId) {
        this.token = token;
        this.memberId = memberId;
    }
}
