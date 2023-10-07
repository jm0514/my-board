package com.jm0514.myboard.global.exception;

public class AuthException extends RuntimeException{

    public AuthException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
    }
}
