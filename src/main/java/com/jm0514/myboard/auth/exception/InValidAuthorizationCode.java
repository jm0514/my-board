package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class InValidAuthorizationCode extends AuthException {

    private static final String MESSAGE = "잘못된 인가 코드입니다.";
    public InValidAuthorizationCode() {
        super(MESSAGE);
    }
}
