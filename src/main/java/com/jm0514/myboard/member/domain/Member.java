package com.jm0514.myboard.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
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

    @Builder
    private Member(
            final String loginAccountId,
            final String name,
            final String profileImageUrl,
            final RoleType roleType
    ) {
        this.loginAccountId = loginAccountId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.roleType = roleType;
        this.createdAt = LocalDateTime.now();
    }

    public void modifyMember(final String name, final String profileImageUrl){
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.modifiedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
