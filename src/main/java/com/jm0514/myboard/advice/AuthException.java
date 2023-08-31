package com.jm0514.myboard.advice;

public class AuthException extends RuntimeException{

    public AuthException(String message) {
        super(message);
    }
}
