package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class ExpiredPeriodJwtException extends AuthException {

    private static final String MESSAGE = "기한이 만료된 토큰입니다.";
    public ExpiredPeriodJwtException() {
        super(MESSAGE);
    }
}
