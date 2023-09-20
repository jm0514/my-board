package com.jm0514.myboard.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponseDto {

    private String name;
    private String profileImageUrl;
    private LocalDateTime createdAt;

    public MemberInfoResponseDto(
            final String name,
            final String profileImageUrl,
            final LocalDateTime createdAt
    ) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
    }

    public static MemberInfoResponseDto of(
            final String name,
            final String profileImageUrl,
            final LocalDateTime createdAt
    ) {
        return new MemberInfoResponseDto(name, profileImageUrl, createdAt);
    }
}
