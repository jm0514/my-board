package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class NotExistTokenException extends AuthException {

    private static final String MESSAGE = "해당 토큰이 존재하지 않습니다.";

    public NotExistTokenException() {
        super(MESSAGE);
    }
}
