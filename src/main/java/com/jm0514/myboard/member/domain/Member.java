package com.jm0514.myboard.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String loginAccountId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Member(final String loginAccountId, final String name, final String profileImageUrl) {
        this.loginAccountId = loginAccountId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.roleType = RoleType.USER;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}
