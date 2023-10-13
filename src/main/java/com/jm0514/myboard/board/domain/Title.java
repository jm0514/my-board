package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

    private static final int TITLE_MIN_LENGTH = 1;
    private static final int TITLE_MAX_LENGTH = 50;

    @Column(name = "title", nullable = false)
    private String value;

    public Title(final String value) {
        validateTitle(value);
        this.value = value;
    }

    private void validateTitle(final String value) throws BadRequestException {
        if (value == null) {
            throw new BadRequestException(TITLE_IS_NULL_EXCEPTION);
        }
        if (value.trim().length() < TITLE_MIN_LENGTH) {
            throw new BadRequestException(TITLE_MIN_LENGTH_EXCEPTION);
        }
        if (value.length() > TITLE_MAX_LENGTH) {
            throw new BadRequestException(TITLE_MAX_LENGTH_EXCEPTION);
        }
    }

    public static Title of(final String value) {
        return new Title(value);
    }
}
