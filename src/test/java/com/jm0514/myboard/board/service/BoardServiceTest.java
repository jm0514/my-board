package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void writeBoard(){
        // given
        BoardRequestDto requestDto = new BoardRequestDto("제목", "내용입니다.");
        Long memberId = getSavedMember().getId();
        Board writtenBoard = requestDto.toEntity(requestDto.getTitle(), requestDto.getContent(), getSavedMember());
        boardRepository.save(writtenBoard);

        // when
        BoardResponseDto result = boardService.writeBoard(memberId, requestDto);

        // then
        assertThat(result).extracting("title", "content")
                .containsExactly("제목", "내용입니다.");
    }

    private Member getSavedMember() {
        Member member = Member.builder()
                .loginAccountId("1234")
                .name("jeong-min")
                .profileImageUrl("https://newsimg.sedaily.com/2023/07/19/29S6XZABI3_1.jpg")
                .roleType(RoleType.USER)
                .build();
        return memberRepository.save(member);
    }
}