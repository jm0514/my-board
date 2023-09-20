package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.board.exception.ContentIsNullException;
import com.jm0514.myboard.board.exception.ContentLengthOverLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentTest {

    @DisplayName("게시글 내용이 null이면 예외를 발생한다.")
    @Test
    void ContentLengthIsNullOrBlank(){
        // given when then
        assertThatThrownBy(() -> new Content(null))
                .isExactlyInstanceOf(ContentIsNullException.class)
                .hasMessage("글 내용은 1글자 이상 입력해야 합니다.");
    }

    @DisplayName("게시글 내용이 3000자 초과 시 예외를 발생한다.")
    @Test
    void ContentLengthVeryLong(){
        // given
        String veryLongContent = "A".repeat(3001);
        // when then
        assertThatThrownBy(() -> new Content(veryLongContent))
                .isExactlyInstanceOf(ContentLengthOverLimitException.class)
                .hasMessage("글 내용은 3000자 미만으로 작성해야 합니다.");
    }
}