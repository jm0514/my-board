package com.jm0514.myboard.member.repository;

import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("소셜 로그인한 계정의 ID를 통해서 로그인한 유저의 정보를 얻을 수 있다.")
    @Test
    void findByMemberInfo(){
        // given
        Member createMember = Member.builder()
                .loginAccountId("1234")
                .name("jeong-min")
                .profileImageUrl("https://newsimg.sedaily.com/2023/07/19/29S6XZABI3_1.jpg")
                .roleType(RoleType.USER)
                .build();
        memberRepository.save(createMember);

        // when
        Member findMember = memberRepository.findByLoginAccountId("1234")
                .orElseGet(() -> memberRepository.save(createMember));

        // then
        assertThat(findMember).isEqualTo(createMember);
    }
}