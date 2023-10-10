package com.jm0514.myboard.comment.dto;

import com.jm0514.myboard.auth.Login;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    @NotBlank(message = "댓글 내용은 1 글자 이상이어야 합니다.")
    @Length(max = 500, message = "댓글 내용은 500 자를 초과할 수 없습니다.")
    private String commentContent;

}
