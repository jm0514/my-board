package com.jm0514.myboard.board.repository;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.like.domain.PostLike;
import com.jm0514.myboard.like.repository.PostLikeRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_BOARD_EXCEPTION;
import static com.jm0514.myboard.member.domain.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;

class BoardRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("총 좋아요의 개수가 감소한다.")
    @Test
    void decreaseTotalLikeCount(){

        // given
        Member createdMember1 = getMember1();
        Member createdMember2 = getMember2();
        memberRepository.save(createdMember1);
        memberRepository.save(createdMember2);

        Board createdBoard = getBoard(createdMember1);
        boardRepository.save(createdBoard);
        Long boardId = createdBoard.getId();

        PostLike postLike1 = getPostLike(createdMember1, createdBoard);
        PostLike postLike2 = getPostLike(createdMember2, createdBoard);
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);
        boardRepository.increaseTotalLikeCount(boardId);
        boardRepository.increaseTotalLikeCount(boardId);

        // when
        boardRepository.decreaseTotalLikeCount(boardId);
        Board result = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));

        // then
        assertThat(result.getTotalLikeCount()).isEqualTo(1);
    }

    @DisplayName("좋아요 총 개수가 증가한다.")
    @Test
    void increaseTotalLikeCount(){
        // given
        Member createdMember1 = getMember1();
        Member createdMember2 = getMember2();
        memberRepository.save(createdMember1);
        memberRepository.save(createdMember2);

        Board createdBoard = getBoard(createdMember1);
        boardRepository.save(createdBoard);
        Long boardId = createdBoard.getId();

        PostLike postLike1 = getPostLike(createdMember1, createdBoard);
        PostLike postLike2 = getPostLike(createdMember2, createdBoard);
        postLikeRepository.save(postLike1);
        postLikeRepository.save(postLike2);

        // when
        boardRepository.increaseTotalLikeCount(boardId);
        boardRepository.increaseTotalLikeCount(boardId);
        Board result = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));

        // then
        assertThat(result.getTotalLikeCount()).isEqualTo(2);

    }

    private PostLike getPostLike(
            final Member createdMember,
            final Board createdBoard
    ) {
        return PostLike.builder()
                .member(createdMember)
                .board(createdBoard)
                .build();
    }

    private Board getBoard(final Member createdMember) {
        return Board.builder()
                .member(createdMember)
                .title("제목")
                .content("내용")
                .build();
    }

    private Member getMember1() {
        return Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .profileImageUrl("URL")
                .roleType(USER)
                .build();
    }

    private Member getMember2() {
        return Member.builder()
                .loginAccountId("12345")
                .name("민정")
                .profileImageUrl("URL")
                .roleType(USER)
                .build();
    }
}