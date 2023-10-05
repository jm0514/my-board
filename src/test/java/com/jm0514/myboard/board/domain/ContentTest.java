package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.advice.BadRequestException;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jm0514.myboard.global.exception.ExceptionStatus.CONTENT_IS_NULL_EXCEPTION;
import static com.jm0514.myboard.global.exception.ExceptionStatus.CONTENT_LENGTH_OVER_LIMIT_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class ContentTest {

    @DisplayName("게시글 내용이 null이면 예외를 발생한다.")
    @Test
    void ContentLengthIsNullOrBlank(){
        // given when then
        assertThatThrownBy(() -> new Content(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(CONTENT_IS_NULL_EXCEPTION.getMessage());
    }

    @DisplayName("게시글 내용이 3000자 초과 시 예외를 발생한다.")
    @Test
    void ContentLengthVeryLong(){
        // given
        String veryLongContent = "A".repeat(3001);
        // when then
        assertThatThrownBy(() -> new Content(veryLongContent))
                .isExactlyInstanceOf(BadRequestException.class)
                .hasMessage(CONTENT_LENGTH_OVER_LIMIT_EXCEPTION.getMessage());
    }
}