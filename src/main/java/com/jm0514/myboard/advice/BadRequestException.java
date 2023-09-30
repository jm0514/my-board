package com.jm0514.myboard.advice;

import com.jm0514.myboard.global.exception.ExceptionStatus;

public class BadRequestException extends BusinessException {

    public BadRequestException(final ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
