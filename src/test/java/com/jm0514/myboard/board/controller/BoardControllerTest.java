package com.jm0514.myboard.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.service.BoardService;
import com.jm0514.myboard.global.ControllerTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BoardControllerTest extends ControllerTest {

    private final static String REFRESH_TOKEN = "refreshToken";
    private final static String ACCESS_TOKEN = "Bearer accessToken";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setUp () {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        given(jwtProvider.getParseClaims(any())).willReturn(new AuthInfo(1L, "USER"));
    }

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void writeBoard() throws Exception {
        // given
        MemberTokens memberTokens = new MemberTokens(REFRESH_TOKEN, ACCESS_TOKEN);
        Cookie cookie = new Cookie("refresh-token", memberTokens.getRefreshToken());
        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .title("제목")
                .content("내용입니다.")
                .createdAt(LocalDateTime.of(2023, 9, 16, 20, 30))
                .build();

        given(boardService.writeBoard(anyLong(), any(BoardRequestDto.class)))
                .willReturn(boardResponseDto);

        BoardRequestDto requestDto = new BoardRequestDto("제목", "내용입니다.");
        ResultActions resultActions = mockMvc.perform(post("/boards")
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .cookie(cookie)
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(document(
                        "Post Board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("엑세스 토큰")
                        ),
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("리프레시 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("글 내용")
                        )
                        ,responseFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("글 내용"),
                                fieldWithPath("createdAt")
                                        .type(JsonFieldType.STRING)
                                        .description("글이 작성된 시간")
                        ))
                )
                .andReturn();

        BoardResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BoardResponseDto.class
        );

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(boardResponseDto);
    }
}