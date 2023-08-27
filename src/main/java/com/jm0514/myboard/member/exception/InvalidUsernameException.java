package com.jm0514.myboard.member.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class InvalidUsernameException extends BadRequestException {

    private static final String MESSAGE = "잘못된 이름입니다.";

    public InvalidUsernameException() {
        super(MESSAGE);
    }
}
