package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class BoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void writeBoard(){
        // given
        BoardRequestDto requestDto = new BoardRequestDto("제목", "내용입니다.");
        Board writtenBoard = requestDto.toEntity(requestDto.getTitle(),
                requestDto.getContent(),
                getSavedMember()
        );
        boardRepository.save(writtenBoard);
        Long memberId = getSavedMember().getId();

        // when
        BoardResponseDto result = boardService.writeBoard(memberId, requestDto);

        // then
        assertThat(result).extracting("title", "content")
                .containsExactly("제목", "내용입니다.");
    }

    @DisplayName("해당 게시글을 찾을 수 있다.")
    @Test
    void findBoard(){
        // given
        Board createdBoard = Board.builder()
                .title("제목")
                .content("내용입니다.")
                .build();
        boardRepository.save(createdBoard);

        // when
        BoardTotalInfoResponse result = boardService.findBoard(createdBoard.getId());

        // then
        assertThat(result).extracting("title", "content")
                .containsExactly("제목", "내용입니다.");
    }

    @DisplayName("해당 게시글을 수정할 수 있다.")
    @Test
    void modifyBoard(){
        // given
        Member savedMember = getSavedMember();
        Long memberId = savedMember.getId();

        BoardRequestDto requestDto = new BoardRequestDto("제목", "내용입니다.");
        Board writtenBoard = requestDto.toEntity(requestDto.getTitle(),
                requestDto.getContent(),
                savedMember
        );
        boardRepository.save(writtenBoard);

        // when then
        boardService.modifyBoard(memberId, 1L, requestDto);
    }

    private Member getSavedMember() {
        Member member = Member.builder()
                .loginAccountId("1234")
                .name("jeong-min")
                .profileImageUrl("https://jeong-min.jpg")
                .roleType(RoleType.USER)
                .build();
        return memberRepository.save(member);
    }
}