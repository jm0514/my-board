package com.jm0514.myboard.board.exception;

import com.jm0514.myboard.advice.BadRequestException;

public class TitleLengthIsNullException extends BadRequestException {

    private static final String MESSAGE = "제목은 공백을 제외하고 1단어 이상 입력해야 합니다.";

    public TitleLengthIsNullException() {
        super(MESSAGE);
    }
}
