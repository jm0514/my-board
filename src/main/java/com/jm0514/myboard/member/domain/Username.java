package com.jm0514.myboard.member.domain;

import com.jm0514.myboard.member.exception.InvalidUsernameException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Username {

    private static final String USERNAME_REGEX = "^[a-zA-Z가-힣]*$";
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    @Column(name = "username")
    private String value;

    public Username(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH
                || !USERNAME_PATTERN.matcher(value).matches()) {
            throw new InvalidUsernameException();
        }
    }
}
