package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.jm0514.myboard.global.exception.ExceptionStatus.CONTENT_IS_NULL_EXCEPTION;
import static com.jm0514.myboard.global.exception.ExceptionStatus.CONTENT_LENGTH_OVER_LIMIT_EXCEPTION;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private static final int CONTENT_MAX_LENGTH = 3000;

    @Lob
    @Column(name = "content", nullable = false)
    private String value;

    public Content(final String value){
        validateContent(value);
        this.value = value;
    }

    private void validateContent(final String value) {
        if (value == null) {
            throw new BadRequestException(CONTENT_IS_NULL_EXCEPTION);
        }
        if (value.length() > CONTENT_MAX_LENGTH) {
            throw new BadRequestException(CONTENT_LENGTH_OVER_LIMIT_EXCEPTION);
        }
    }

    public static Content of(final String value) {
        return new Content(value);
    }
}
