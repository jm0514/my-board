package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class InvalidJwtException extends AuthException {

    private static final String MESSAGE ="잘못된 형식의 토큰입니다.";

    public InvalidJwtException() {
        super(MESSAGE);
    }
}
