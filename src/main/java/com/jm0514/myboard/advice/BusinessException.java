package com.jm0514.myboard.advice;

public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
