package com.jm0514.myboard.auth.dto;

import com.jm0514.myboard.member.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthInfo {

    private Long id;
    private String roleType;
}
