package com.jm0514.myboard.comment.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentContent {

    private static final int COMMENT_CONTENT_MAX_LENGTH = 500;
    private static final int COMMENT_CONTENT_MIN_LENGTH = 1;

    private String value;

    public CommentContent(final String value) {
        validationCommentContent(value);
        this.value = value;
    }

    private void validationCommentContent(final String value) {
        if (value == null) {
            throw new BadRequestException(COMMENT_IS_NULL_EXCEPTION);
        }
        if (value.trim().length() < COMMENT_CONTENT_MIN_LENGTH) {
            throw new BadRequestException(COMMENT_MIN_LENGTH_EXCEPTION);
        }
        if (value.length() > COMMENT_CONTENT_MAX_LENGTH) {
            throw new BadRequestException(COMMENT_MAX_LENGTH_EXCEPTION);
        }
    }
}
