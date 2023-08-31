package com.jm0514.myboard.auth.exception;

import com.jm0514.myboard.advice.AuthException;

public class InvalidOauthService extends AuthException {

    private static final String MESSAGE = "지원하지 않는 서비스입니다.";

    public InvalidOauthService() {
        super(MESSAGE);
    }
}
