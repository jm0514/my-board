package com.jm0514.myboard.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionStatus {

    CONTENT_IS_NULL_EXCEPTION("글 내용이 null 입니다."),
    CONTENT_MIN_LENGTH_EXCEPTION("글 내용은 1 글자 이상 입력해야 합니다."),
    CONTENT_MAX_LENGTH_EXCEPTION("글 내용은 3000자 미만으로 작성해야 합니다."),
    INVALID_WRITER_EXCEPTION("해당 게시글을 작성한 작성자가 아닙니다."),
    NOT_FOUND_BOARD_EXCEPTION("해당 게시글을 찾을 수 없습니다."),
    TITLE_IS_NULL_EXCEPTION("제목이 null 입니다."),
    TITLE_MIN_LENGTH_EXCEPTION("제목은 공백을 제외하고 1 단어 이상 입력해야 합니다."),
    TITLE_MAX_LENGTH_EXCEPTION("제목은 50자 미만이어야 합니다."),
    COMMENT_IS_NULL_EXCEPTION("댓글이 null 입니다."),
    COMMENT_MIN_LENGTH_EXCEPTION("댓글 내용은 1 글자 이상 입력해야 합니다."),
    COMMENT_MAX_LENGTH_EXCEPTION("댓글 내용은 500자 이하여야 합니다."),

    EMPTY_HEADER_EXCEPTION("헤더에 토큰 값이 없습니다."),
    EXPIRED_PERIOD_JWT_EXCEPTION("기한이 만료된 토큰입니다."),
    INVALID_AUTHORIZATION_CODE("잘못된 인가 코드입니다."),
    INVALID_JWT_EXCEPTION("잘못된 형식의 토큰입니다."),
    INVALID_OAUTH_SERVICE("지원하지 않는 서비스입니다."),
    INVALID_REFRESH_TOKEN_EXCEPTION("유효하지 않은 리프레시 토큰입니다."),
    INVALID_TOKEN_EXCEPTION("토큰 형식이 잘못 되었습니다."),

    NOT_FOUND_MEMBER_EXCEPTION("해당 회원이 존재하지 않습니다."),

    INTERNAL_SERVER_ERROR("서버에 알 수 없는 에러가 발생했습니다.");

    private final String message;
}
