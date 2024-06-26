package com.jm0514.myboard.board.domain;

import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_created_at", columnList = "createdAt"))
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int totalLikeCount = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Version
    private Long version;

    @Builder
    private Board(final String title, final String content, final Member member) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public void modifyBoard(final String title, final String content) {
        this.title = Title.of(title);
        this.content = Content.of(content);
        this.modifiedAt = LocalDateTime.now();
    }

    public void likeUp () {
        this.totalLikeCount += 1;
    }

    public void likeDown() {
        this.totalLikeCount -= 1;
    }

    public Member findWriter() {
        return member;
    }

    public String getValidateTitle() {
        return title.getValue();
    }

    public String getValidateContent() {
        return content.getValue();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getTotalLikeCount() {
        return totalLikeCount;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public String getMemberName() {
        return member.getName();
    }
}
