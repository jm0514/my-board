package com.jm0514.myboard.comment.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentContentTest {

    @DisplayName("댓글 내용이 null 일 경우 예외를 발생한다.")
    @Test
    void commentContentIsNullException() {
        // given when then
        assertThatThrownBy(() -> new CommentContent(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(COMMENT_IS_NULL_EXCEPTION.getMessage());
    }

    @DisplayName("댓글 내용이 공백을 제외하고 1 글자 이상이 아닐 시 예외를 발생한다.")
    @Test
    void commentContentIsEmptyStringException() {
        // given
        String emptyString = "   ";
        // when then
        assertThatThrownBy(() -> new CommentContent(emptyString))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(COMMENT_MIN_LENGTH_EXCEPTION.getMessage());
    }

    @DisplayName("댓글 내용이 500 자를 초과했을 때 예외가 발생한다.")
    @Test
    void commentContentVeryLongException() {
        // given
        String longString = "a".repeat(501);
        // when then
        assertThatThrownBy(() -> new CommentContent(longString))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(COMMENT_MAX_LENGTH_EXCEPTION.getMessage());
    }

}