package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.board.exception.ContentIsNullException;
import com.jm0514.myboard.board.exception.ContentLengthOverLimitException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
            throw new ContentIsNullException();
        }
        if (value.length() > CONTENT_MAX_LENGTH) {
            throw new ContentLengthOverLimitException();
        }
    }

}
