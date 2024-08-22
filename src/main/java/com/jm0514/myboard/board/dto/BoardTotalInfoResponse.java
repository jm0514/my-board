package com.jm0514.myboard.board.dto;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.comment.dto.CommentResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(of = "boardId")
@NoArgsConstructor
public class BoardTotalInfoResponse {

    private Long boardId;

    private String title;

    private String content;

    private String memberName;

    private LocalDateTime createdAt;

    private List<CommentResponse> comments;

    private int totalLikeCount = 0;

    @Builder
    public BoardTotalInfoResponse(
            final Long boardId,
            final String title,
            final String content,
            final String memberName,
            final LocalDateTime createdAt,
            final List<CommentResponse> comments,
            final int totalLikeCount
    ) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.memberName = memberName;
        this.createdAt = createdAt;
        this.comments = comments;
        this.totalLikeCount = totalLikeCount;
    }

    public BoardTotalInfoResponse(
            final Long boardId,
            final String title,
            final String content,
            final String memberName,
            final LocalDateTime createdAt,
            final int totalLikeCount
    ) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.memberName = memberName;
        this.createdAt = createdAt;
        this.totalLikeCount = totalLikeCount;
    }

    public static BoardTotalInfoResponse of(
            final Board board,
            final List<Comment> comments
    ) {
        return BoardTotalInfoResponse.builder()
                .boardId(board.getId())
                .title(board.getValidateTitle())
                .content(board.getValidateContent())
                .memberName(board.getMemberName())
                .comments(comments.stream()
                        .map(CommentResponse::of)
                        .collect(Collectors.toList()))
                .createdAt(board.getCreatedAt())
                .totalLikeCount(board.getTotalLikeCount())
                .build();
    }

    public static BoardTotalInfoResponse writeBoard(final Board board) {
        return BoardTotalInfoResponse.builder()
                .boardId(board.getId())
                .title(board.getValidateTitle())
                .content(board.getValidateContent())
                .memberName(board.getMemberName())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
