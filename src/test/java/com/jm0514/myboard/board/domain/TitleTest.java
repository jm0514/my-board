package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @DisplayName("제목이 null로 들어오면 예외를 발생한다.")
    @Test
    void titleIsNotBlankException() {
        // given when then
        assertThatThrownBy(() -> new Title(null))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage(TITLE_IS_NULL_EXCEPTION.getMessage());
    }

    @DisplayName("제목은 공백을 제외하고 1 글자 이상이 아니라면 예외가 발생한다.")
    @Test
    void titleIsEmptyStringException() {
        // given
        String emptyStringTitle = "   ";

        // when then
        assertThatThrownBy(() -> new Title(emptyStringTitle))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(TITLE_MIN_LENGTH_EXCEPTION.getMessage());
    }

    @DisplayName("제목이 50자를 초과할 경우 예외를 발생한다.")
    @Test
    void titleLengthLessThan50Exception() {
        // given
        String title = "A".repeat(51);

        // when then
        assertThatThrownBy(() -> new Title(title))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage(TITLE_MAX_LENGTH_EXCEPTION.getMessage());
    }
}