package com.jm0514.myboard.member.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class NotFoundMemberException extends BadRequestException {

    private static final String MESSAGE = "해당 회원이 존재하지 않습니다.";

    public NotFoundMemberException() {
        super(MESSAGE);
    }
}
