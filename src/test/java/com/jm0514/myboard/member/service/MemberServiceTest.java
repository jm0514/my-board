package com.jm0514.myboard.member.service;

import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.dto.MemberInfoRequestDto;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_MEMBER_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest extends IntegrationTestSupport {

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
        Member savedMember = getSavedMember();
        Long memberId = savedMember.getId();
        AuthInfo authInfo = new AuthInfo(memberId, "USER");

        // when
        MemberInfoResponseDto result = memberService.findMemberInfo(authInfo);

        // then
        assertThat(result).extracting("name", "profileImageUrl")
                .contains("jeong-min", "https://jeong-min.jpg");
    }

    @DisplayName("로그인한 계정의 회원 정보를 변경할 수 있다.")
    @Test
    void updateMemberByAuthInfo(){
        // given
        Member savedMember = getSavedMember();
        Long memberId = savedMember.getId();
        AuthInfo authInfo = new AuthInfo(memberId, "USER");

        MemberInfoRequestDto request = new MemberInfoRequestDto("min", "https://min-jeong.jpg");

        // when
        memberService.updateMember(authInfo, request);
        Member updatedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));

        // then
        assertThat(updatedMember).extracting("name", "profileImageUrl")
                .contains("min", "https://min-jeong.jpg");
    }

    private Member getSavedMember() {
        Member member = Member.builder()
                .loginAccountId("123")
                .name("jeong-min")
                .profileImageUrl("https://jeong-min.jpg")
                .roleType(RoleType.USER)
                .build();
        return memberRepository.save(member);
    }
}