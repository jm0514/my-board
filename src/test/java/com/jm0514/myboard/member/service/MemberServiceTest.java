package com.jm0514.myboard.member.service;

import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("로그인한 계정의 회원 정보를 불러온다.")
    @Test
    void findMemberByAuthInfo(){
        // given
        Member member = Member.builder()
                .loginAccountId("1234")
                .name("jeong-min")
                .profileImageUrl("https://newsimg.sedaily.com/2023/07/19/29S6XZABI3_1.jpg")
                .roleType(RoleType.USER)
                .build();
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();
        AuthInfo authInfo = new AuthInfo(memberId, "USER");

        // when
        MemberInfoResponseDto result = memberService.findMemberInfo(authInfo);

        // then
        assertThat(result).extracting("name", "profileImageUrl")
                .contains("jeong-min", "https://newsimg.sedaily.com/2023/07/19/29S6XZABI3_1.jpg");
    }
}