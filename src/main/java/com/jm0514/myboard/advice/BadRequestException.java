package com.jm0514.myboard.advice;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message);
    }
}
