package com.jm0514.myboard.comment.dto;

import com.jm0514.myboard.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private String content;
    private String commenter;
    private LocalDateTime createdAt;

    @Builder
    public CommentResponse(
            final String content,
            final String commenter,
            final LocalDateTime createdAt
    ) {
        this.content = content;
        this.commenter = commenter;
        this.createdAt = createdAt;
    }

    public static CommentResponse of(final Comment comment) {
        return CommentResponse.builder()
                .content(comment.getValidatedComment())
                .commenter(comment.getCommenter())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
