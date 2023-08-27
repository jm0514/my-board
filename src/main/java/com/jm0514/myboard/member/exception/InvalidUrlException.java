package com.jm0514.myboard.member.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class InvalidUrlException extends BadRequestException {

    private final static String MESSAGE = "잘못된 형식의 Url입니다.";

    public InvalidUrlException() {
        super(MESSAGE);
    }
}
