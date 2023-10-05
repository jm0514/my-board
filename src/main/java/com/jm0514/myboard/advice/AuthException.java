package com.jm0514.myboard.advice;

import com.jm0514.myboard.global.exception.ExceptionStatus;

public class AuthException extends RuntimeException{

    public AuthException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
    }
}
