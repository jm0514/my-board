package com.jm0514.myboard.member.domain;

import com.jm0514.myboard.member.exception.InvalidEmailException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    @Column(name = "email")
    private String value;

    public Email(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH
                || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException();
        }
    }
}
