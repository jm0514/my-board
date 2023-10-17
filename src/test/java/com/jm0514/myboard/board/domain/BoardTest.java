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
}