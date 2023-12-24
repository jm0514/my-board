package com.jm0514.myboard.board.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @DisplayName("게시글을 수정할 수 있다.")
    @Test
    void modifyBoard(){
        // given
        Board writtenBoard = Board.builder()
                .title("제목")
                .content("내용입니다.")
                .build();

        // when
        writtenBoard.modifyBoard("수정된 제목", "수정된 내용입니다.");

        // then
        assertThat(writtenBoard.getValidateTitle()).isEqualTo("수정된 제목");
        assertThat(writtenBoard.getValidateContent()).isEqualTo("수정된 내용입니다.");
    }

    @DisplayName("좋아요 총 개수가 증가한다.")
    @Test
    void likeUp(){
        // given
        Board writtenBoard = Board.builder()
                .title("제목")
                .content("내용입니다.")
                .build();

        // when
        writtenBoard.likeUp();

        // then
        assertThat(writtenBoard.getTotalLikeCount()).isEqualTo(1);
    }

    @DisplayName("좋아요 총 개수가 감소한다.")
    @Test
    void likeDown(){
        // given
        Board writtenBoard = Board.builder()
                .title("제목")
                .content("내용입니다.")
                .build();

        // when
        writtenBoard.likeUp();
        writtenBoard.likeDown();

        // then
        assertThat(writtenBoard.getTotalLikeCount()).isEqualTo(0);
    }
}