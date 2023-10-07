package com.jm0514.myboard.global.exception;

public class BadRequestException extends BusinessException {

    public BadRequestException(final ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
