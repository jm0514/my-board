package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jm0514.myboard.global.exception.ExceptionStatus.TITLE_LENGTH_IS_NULL_EXCEPTION;
import static com.jm0514.myboard.global.exception.ExceptionStatus.TITLE_LENGTH_OVER_LIMIT_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @DisplayName("제목이 null로 들어오면 예외를 발생한다.")
    @Test
    void titleIsNotBlank(){
        // given when then
        assertThatThrownBy(() -> new Title(null))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage(TITLE_LENGTH_IS_NULL_EXCEPTION.getMessage());
    }

    @DisplayName("제목이 50자를 초과할 경우 예외를 발생한다.")
    @Test
    void titleLengthLessThan50(){
        // given when then
        String title = "A".repeat(51);
        assertThatThrownBy(() -> new Title(title))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage(TITLE_LENGTH_OVER_LIMIT_EXCEPTION.getMessage());
    }
}