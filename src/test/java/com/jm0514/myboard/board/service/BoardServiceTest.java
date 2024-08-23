package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.comment.repository.CommentRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardCacheService boardCacheService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
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
        BoardTotalInfoResponse result = boardService.writeBoard(memberId, requestDto);

        // then
        assertThat(result).extracting("title", "content")
                .containsExactly("제목", "내용입니다.");
    }

    @DisplayName("해당 게시글을 찾을 수 있다.")
    @Test
    void findBoard(){
        // given
        Member savedMember = getSavedMember();
        Board createdBoard = Board.builder()
                .title("제목")
                .content("내용입니다.")
                .member(savedMember)
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
        Long boardId = writtenBoard.getId();

        // when then
        boardService.modifyBoard(memberId, boardId, requestDto);
    }

    @DisplayName("게시글을 최신 순으로 정렬하고 페이징한다.")
    @Test
    void findLimitedBoardList(){
        // given
        Member savedMember = getSavedMember();

        Board board1 = Board.builder()
                .member(savedMember)
                .title("제목 입니다1.")
                .content("내용 입니다1.")
                .build();
        boardRepository.save(board1);

        Board board2 = Board.builder()
                .member(savedMember)
                .title("제목 입니다2.")
                .content("내용 입니다2.")
                .build();
        boardRepository.save(board2);

        Comment comment1 = Comment.builder()
                .board(board1)
                .commentContent("댓글 내용 입니다.1")
                .member(savedMember)
                .build();
        commentRepository.save(comment1);

        Comment comment2 = Comment.builder()
                .board(board2)
                .commentContent("댓글 내용 입니다.2")
                .member(savedMember)
                .build();
        commentRepository.save(comment2);

        PageRequest pageRequest = PageRequest.of(0, 10);
        // when
        List<BoardTotalInfoResponse> result = boardService.findLimitedBoardList(pageRequest);

        // then
        assertThat(result.get(0).getTitle()).isEqualTo("제목 입니다2.");
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