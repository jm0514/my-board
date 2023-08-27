package com.jm0514.myboard.member.domain;

import com.jm0514.myboard.member.exception.InvalidUrlException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture {

    private static final String URL_REGEX = "^(http|https)://([a-zA-Z0-9.-]+)\\\\.([a-zA-Z]{2,6})(:[0-9]{1,5})?(/.*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 2083;

    @Column(name = "picture")
    private String value;

    public Picture(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH
                || !URL_PATTERN.matcher(value).matches()) {
            throw new InvalidUrlException();
        }
    }
}
