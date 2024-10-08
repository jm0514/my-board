package com.jm0514.myboard.board.controller;

import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.comment.dto.CommentResponse;
import com.jm0514.myboard.global.IntegrationControllerTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BoardControllerTest extends IntegrationControllerTest {

    private final static String ACCESS_TOKEN = "Bearer accessToken";

    @BeforeEach
    void setUp () {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        given(jwtProvider.getParseClaims(any())).willReturn(new AuthInfo(1L, "USER"));
    }

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void writeBoard() throws Exception {
        // given
        BoardTotalInfoResponse boardResponseDto = BoardTotalInfoResponse.builder()
                .title("제목")
                .content("내용입니다.")
                .memberName("작성자")
                .createdAt(LocalDateTime.of(2023, 9, 16, 20, 30))
                .build();
        given(boardService.writeBoard(anyLong(), any(BoardRequestDto.class)))
                .willReturn(boardResponseDto);

        BoardRequestDto requestDto = new BoardRequestDto("제목", "내용입니다.");
        ResultActions resultActions = mockMvc.perform(post("/boards")
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
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
                        requestFields(
                                fieldWithPath("title")
                                        .type(STRING)
                                        .description("글 제목"),
                                fieldWithPath("content")
                                        .type(STRING)
                                        .description("글 내용")
                        ),
                        responseFields(
                                fieldWithPath("boardId")
                                        .type(LONG)
                                        .description("글 ID"),
                                fieldWithPath("title")
                                        .type(STRING)
                                        .description("글 제목"),
                                fieldWithPath("content")
                                        .type(STRING)
                                        .description("글 내용"),
                                fieldWithPath("memberName")
                                        .type(STRING)
                                        .description("글 작성자"),
                                fieldWithPath("createdAt")
                                        .type(STRING)
                                        .description("글이 작성된 시간"),
                                fieldWithPath("totalLikeCount")
                                        .type(INTEGER)
                                        .description("게시글의 총 좋아요 개수"),
                                fieldWithPath("comments")
                                        .type(NULL)
                                        .description("댓글 목록, 현재 null로 반환됨").optional()
                        )
                ))
                .andReturn();

        BoardResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BoardResponseDto.class
        );

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(boardResponseDto);
    }

    @DisplayName("해당 게시글을 조회할 수 있다.")
    @Test
    void findBoardById() throws Exception {
        // given
        CommentResponse comment1 = CommentResponse.builder()
                .content("댓글 내용입니다.")
                .commenter("작성자")
                .createdAt(LocalDateTime.of(2023, 10, 13, 17, 5))
                .build();
        CommentResponse comment2 = CommentResponse.builder()
                .content("다른 댓글 내용입니다.")
                .commenter("다른 작성자")
                .createdAt(LocalDateTime.of(2023, 10, 13, 21, 5))
                .build();
        List<CommentResponse> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        BoardTotalInfoResponse boardTotalInfoResponse = BoardTotalInfoResponse.builder()
                .title("제목")
                .content("내용입니다.")
                .totalLikeCount(0)
                .memberName("작성자1")
                .comments(comments)
                .createdAt(LocalDateTime.of(2023, 9, 16, 20, 30))
                .build();

        given(boardCacheService.findBoardCache(anyLong()))
                .willReturn(boardTotalInfoResponse);

        ResultActions resultActions = mockMvc.perform(get("/boards/{boardId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("Post Find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("boardId")
                                        .description("게시판 ID")
                        ),
                        responseFields(
                                fieldWithPath("boardId")
                                        .type(LONG)
                                        .description("글 ID"),
                                fieldWithPath("title")
                                        .type(STRING)
                                        .description("글 제목"),
                                fieldWithPath("content")
                                        .type(STRING)
                                        .description("글 내용"),
                                fieldWithPath("memberName")
                                        .type(STRING)
                                        .description("글 작성자"),
                                fieldWithPath("createdAt")
                                        .type(STRING)
                                        .description("글이 작성된 시간"),
                                fieldWithPath("totalLikeCount")
                                        .type(INTEGER)
                                        .description("게시글의 총 좋아요 개수"),

                                fieldWithPath("comments[].boardId")
                                        .type(LONG)
                                        .description("게시글 ID"),
                                fieldWithPath("comments[].content")
                                        .type(STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("comments[].commenter")
                                        .type(STRING)
                                        .description("댓글 작성자"),
                                fieldWithPath("comments[].createdAt")
                                        .type(STRING)
                                        .description("댓글이 작성된 시간")
                        )
                )).andReturn();

        BoardTotalInfoResponse actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BoardTotalInfoResponse.class
        );

        // then
        Assertions.assertThat(actual).usingRecursiveComparison()
                .isEqualTo(boardTotalInfoResponse);
    }

    @DisplayName("해당 게시글을 수정할 수 있다.")
    @Test
    void modifyBoard() throws Exception{
        // given
        BoardRequestDto requestDto = new BoardRequestDto("수정된 제목", "수정된 내용입니다.");

        doNothing().when(boardService)
                .modifyBoard(anyLong(), anyLong(), any(BoardRequestDto.class));

        ResultActions resultActions = mockMvc.perform(patch("/boards/{boardId}", 1)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        );

        // when
        resultActions.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(
                        "Modify Board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("boardId")
                                        .description("게시판 ID")
                        ),
                        requestFields(
                                fieldWithPath("title")
                                        .type(STRING)
                                        .description("수정할 제목"),
                                fieldWithPath("content")
                                        .type(STRING)
                                        .description("수정할 내용")
                        )
                ));

        // then
        verify(boardService).modifyBoard(anyLong(), anyLong(), any(BoardRequestDto.class));
    }

    @DisplayName("게시글을 최신 순으로 정렬하고 페이징")
    @Test
    void findLimitedBoardList() throws Exception {
        // given
        CommentResponse comment1 = CommentResponse.builder()
                .boardId(1L)
                .content("댓글 내용입니다.")
                .commenter("작성자")
                .createdAt(LocalDateTime.of(2023, 10, 13, 17, 5))
                .build();
        CommentResponse comment2 = CommentResponse.builder()
                .boardId(1L)
                .content("다른 댓글 내용입니다.")
                .commenter("다른 작성자")
                .createdAt(LocalDateTime.of(2023, 10, 15, 21, 5))
                .build();
        List<CommentResponse> comments1 = new ArrayList<>();
        List<CommentResponse> comments2 = new ArrayList<>();
        comments1.add(comment1);
        comments1.add(comment2);

        CommentResponse comment3 = CommentResponse.builder()
                .boardId(2L)
                .content("다른 댓글 내용입니다.")
                .commenter("다른 작성자")
                .createdAt(LocalDateTime.of(2023, 10, 15, 21, 5))
                .build();
        comments2.add(comment3);

        BoardTotalInfoResponse boardTotalInfoResponse1 = BoardTotalInfoResponse.builder()
                .boardId(1L)
                .title("제목1")
                .content("내용1")
                .memberName("작성자1")
                .comments(comments1)
                .totalLikeCount(2)
                .createdAt(LocalDateTime.of(2023, 9, 16, 20, 30))
                .build();

        BoardTotalInfoResponse boardTotalInfoResponse2 = BoardTotalInfoResponse.builder()
                .boardId(2L)
                .title("제목2")
                .content("내용2")
                .memberName("작성자2")
                .comments(comments2)
                .totalLikeCount(1)
                .createdAt(LocalDateTime.of(2023, 10, 5, 20, 30))
                .build();

        List<BoardTotalInfoResponse> responseList = new ArrayList<>();
        responseList.add(boardTotalInfoResponse2);
        responseList.add(boardTotalInfoResponse1);

        PageRequest pageRequest = PageRequest.of(0, 1);

        given(boardCacheService.findLimitedBoardListCache(pageRequest)).willReturn(responseList);

        ResultActions resultActions = mockMvc.perform(get("/boards?page=0&size=1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // when then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("Sorted Post List",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].boardId")
                                        .type(LONG)
                                        .description("글 ID"),
                                fieldWithPath("[].title")
                                        .type(STRING)
                                        .description("글 제목"),
                                fieldWithPath("[].content")
                                        .type(STRING)
                                        .description("글 내용"),
                                fieldWithPath("[].memberName")
                                        .type(STRING)
                                        .description("글 작성자"),
                                fieldWithPath("[].createdAt")
                                        .type(STRING)
                                        .description("글이 작성된 시간"),
                                fieldWithPath("[].totalLikeCount")
                                        .type(INTEGER)
                                        .description("게시글의 총 좋아요 개수"),

                                fieldWithPath("[].comments[].boardId")
                                        .type(LONG)
                                        .description("글 ID"),
                                fieldWithPath("[].comments[].content")
                                        .type(STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("[].comments[].commenter")
                                        .type(STRING)
                                        .description("댓글 작성자"),
                                fieldWithPath("[].comments[].createdAt")
                                        .type(STRING)
                                        .description("댓글이 작성된 시간")
                        )
                ));
    }

}