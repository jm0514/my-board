package com.jm0514.myboard.member.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class InvalidEmailException extends BadRequestException {

    private static final String MESSAGE = "잘못된 형식의 이메일입니다.";

    public InvalidEmailException() {
        super(MESSAGE);
    }
}
