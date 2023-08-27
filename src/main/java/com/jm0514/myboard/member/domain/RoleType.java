package com.jm0514.myboard.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER("USER"), ADMIN("ADMIN");

    private final String value;
}
