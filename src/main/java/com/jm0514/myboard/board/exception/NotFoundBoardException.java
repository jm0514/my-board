package com.jm0514.myboard.board.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class NotFoundBoardException extends BadRequestException {

    private static final String MESSAGE = "해당 게시글을 찾을 수 없습니다.";

    public NotFoundBoardException() {
        super(MESSAGE);
    }
}
