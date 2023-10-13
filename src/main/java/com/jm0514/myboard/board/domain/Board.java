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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

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

    public Member findWriter() {
        return member;
    }

    public String getValidateTitle() {
        return title.getValue();
    }

    public String getValidateContent() {
        return content.getValue();
    }

}
