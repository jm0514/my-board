package com.jm0514.myboard.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("회원을 수정할 수 있다.")
    @Test
    void modifyMember(){
        // given
        Member createdMember = Member.builder()
                .name("jeong-min")
                .profileImageUrl("https://jeong-min.jpg")
                .build();

        // when
        createdMember.modifyMember("min-jeong", "https://min-jeong.jpg");

        // then
        assertThat(createdMember).extracting("name", "profileImageUrl")
                .containsExactly("min-jeong", "https://min-jeong.jpg");
    }
}