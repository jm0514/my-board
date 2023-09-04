package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class InvalidTokenException extends AuthException {

    private static final String MESSAGE = "토큰 형식이 잘못 되었습니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
