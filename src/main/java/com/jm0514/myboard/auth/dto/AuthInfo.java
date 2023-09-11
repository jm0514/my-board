package com.jm0514.myboard.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthInfo {

    private Long id;
    private String roleType;
}
