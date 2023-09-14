package com.jm0514.myboard.board.dto;

import com.jm0514.myboard.board.domain.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {

    private String title;

    private String content;

    public BoardResponseDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(board.getTitle().getValue(), board.getContent().getValue());
    }
}
