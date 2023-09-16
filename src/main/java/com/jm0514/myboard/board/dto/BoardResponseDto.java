package com.jm0514.myboard.board.dto;

import com.jm0514.myboard.board.domain.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        String getTitle = board.getTitle().getValue();
        String getContent = board.getContent().getValue();
        LocalDateTime getCreatedTime = board.getCreatedAt();
        return BoardResponseDto.builder()
                .title(getTitle)
                .content(getContent)
                .createdAt(getCreatedTime)
                .build();
    }
}
