package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.board.exception.TitleLengthIsNullException;
import com.jm0514.myboard.board.exception.TitleLengthOverLimitException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    private void validateTitle(final String value) {
        if (value == null) {
            throw new TitleLengthIsNullException();
        }
        if (value.trim().length() < TITLE_MIN_LENGTH) {
            throw new TitleLengthIsNullException();
        }
        if (value.length() > TITLE_MAX_LENGTH) {
            throw new TitleLengthOverLimitException();
        }
    }
}
