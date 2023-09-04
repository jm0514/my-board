package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class InvalidRefreshTokenException extends AuthException {

    private static final String MESSAGE = "유효하지 않은 리프레시 토큰입니다.";

    public InvalidRefreshTokenException() {
        super(MESSAGE);
    }
}
