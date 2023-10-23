package com.jm0514.myboard.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLikeResponse {

    private boolean like;

    public PostLikeResponse(final boolean like) {
        this.like = like;
    }
}
