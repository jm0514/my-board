package com.jm0514.myboard.board.dto;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {

    @NotBlank(message = "제목은 1글자 이상이어야 합니다.")
    @Length(max = 50, message = "제목은 50글자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "글 내용은 1글자 이상이어야 합니다.")
    @Length(max = 3000, message = "글 내용은 3000자 이하여야 합니다.")
    private String content;

    public BoardRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Board toEntity(String title, String content, Member member) {
        return Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
