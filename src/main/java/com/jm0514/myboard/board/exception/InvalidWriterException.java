package com.jm0514.myboard.board.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class InvalidWriterException extends BadRequestException {

    private static final String MESSAGE = "해당 게시글을 작성한 작성자가 아닙니다.";

    public InvalidWriterException() {
        super(MESSAGE);
    }
}
