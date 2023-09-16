package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.board.exception.TitleLengthIsNullException;
import com.jm0514.myboard.board.exception.TitleLengthOverLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @DisplayName("제목이 null로 들어오면 예외를 발생한다.")
    @Test
    void titleIsNotBlank(){
        // given when then
        assertThatThrownBy(() -> new Title(null))
                .isExactlyInstanceOf(TitleLengthIsNullException.class)
                .hasMessage("제목은 공백을 제외하고 1단어 이상 입력해야 합니다.");
    }

    @DisplayName("제목이 50자를 초과할 경우 예외를 발생한다.")
    @Test
    void titleLengthLessThan50(){
        // given when then
        String title = "A".repeat(51);
        assertThatThrownBy(() -> new Title(title))
                .isExactlyInstanceOf(TitleLengthOverLimitException.class)
                .hasMessage("제목은 50자 미만이어야 합니다.");
    }
}