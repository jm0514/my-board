package com.jm0514.myboard.member.repository;

import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_MEMBER_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("소셜 로그인한 계정의 ID를 통해서 로그인한 유저의 정보를 얻을 수 있다.")
    @Test
    void findByMemberInfo(){
        // given
        Member createMember = Member.builder()
                .loginAccountId("1234")
                .name("jeong-min")
                .profileImageUrl("https://jeong-min.jpg")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(createMember);

        // when
        Member findMember = memberRepository.findByLoginAccountId("1234")
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));

        // then
        assertThat(findMember).isEqualTo(createMember);
    }
}