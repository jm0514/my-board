package com.jm0514.myboard.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final String message;

    public BusinessException(final ExceptionStatus exceptionStatus) {
        this.message = exceptionStatus.getMessage();
    }
}
