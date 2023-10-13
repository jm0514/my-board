package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class ContentTest {

    @DisplayName("게시글 내용이 null이면 예외를 발생한다.")
    @Test
    void contentLengthIsNullOrBlankException() {
        // given when then
        assertThatThrownBy(() -> new Content(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(CONTENT_IS_NULL_EXCEPTION.getMessage());
    }

    @DisplayName("게시글 내용은 공백을 제외하고 1글자 이상이 아닐 시 예외가 발생한다.")
    @Test
    void contentIsEmptyStringException() {
        // given
        String emptyStringContent = "   ";
        // when then
        assertThatThrownBy(() -> new Content(emptyStringContent))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(CONTENT_MIN_LENGTH_EXCEPTION.getMessage());
    }

    @DisplayName("게시글 내용이 3000자 초과 시 예외를 발생한다.")
    @Test
    void contentLengthVeryLongException() {
        // given
        String veryLongContent = "A".repeat(3001);
        // when then
        assertThatThrownBy(() -> new Content(veryLongContent))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage(CONTENT_MAX_LENGTH_EXCEPTION.getMessage());
    }
}