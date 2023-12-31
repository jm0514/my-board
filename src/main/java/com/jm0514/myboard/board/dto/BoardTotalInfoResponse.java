package com.jm0514.myboard.board.dto;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.comment.dto.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardTotalInfoResponse {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private List<CommentResponse> comments;

    private int totalLikeCount = 0;

    @Builder
    public BoardTotalInfoResponse(
            final String title,
            final String content,
            final LocalDateTime createdAt,
            final List<CommentResponse> comments,
            final int totalLikeCount
    ) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.comments = comments;
        this.totalLikeCount = totalLikeCount;
    }

    public static BoardTotalInfoResponse of(
            final Board board,
            final List<CommentResponse> comments
    ) {
        return BoardTotalInfoResponse.builder()
                .title(board.getValidateTitle())
                .content(board.getValidateContent())
                .comments(comments)
                .createdAt(board.getCreatedAt())
                .totalLikeCount(board.getTotalLikeCount())
                .build();
    }
}
