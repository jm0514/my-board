package com.jm0514.myboard.board.dto;

import com.jm0514.myboard.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    @Builder
    public BoardResponseDto(
            final String title,
            final String content,
            final LocalDateTime createdAt
    ) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .title(board.getValidateTitle())
                .content(board.getValidateContent())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
