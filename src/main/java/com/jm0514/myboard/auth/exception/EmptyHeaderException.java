package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class EmptyHeaderException extends AuthException {

    private static final String MESSAGE = "헤더에 토큰 값이 없습니다.";

    public EmptyHeaderException() {
        super(MESSAGE);
    }
}
