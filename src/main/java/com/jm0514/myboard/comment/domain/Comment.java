package com.jm0514.myboard.comment.domain;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;

    @Embedded
    private CommentContent commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Comment(final String commentContent,
                    final Board board,
                    final Member member
    ) {
        this.commentContent = new CommentContent(commentContent);
        this.board = board;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public static Comment of(final String commentContent,
                             final Board board,
                             final Member member
    ) {
        return new Comment(commentContent, board, member);
    }

    public String getValidatedComment() {
        return commentContent.getValue();
    }

    public String getCommenter() {
        return member.getName();
    }
}
