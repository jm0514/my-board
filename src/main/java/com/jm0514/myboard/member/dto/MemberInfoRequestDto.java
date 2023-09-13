package com.jm0514.myboard.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoRequestDto {

    @NotBlank(message = "닉네임을 공백으로 등록할 수 없습니다.")
    @Size(max = 15, message = "닉네임은 15글자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "프로필 사진은 필수입니다.")
    private String profileImageUrl;

    public MemberInfoRequestDto(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
