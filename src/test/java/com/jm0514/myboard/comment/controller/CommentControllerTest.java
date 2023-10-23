package com.jm0514.myboard.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.comment.dto.CommentRequest;
import com.jm0514.myboard.comment.service.CommentService;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class CommentControllerTest extends ControllerTest {

    private final static String REFRESH_TOKEN = "refreshToken";
    private final static String ACCESS_TOKEN = "Bearer accessToken";

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        given(jwtProvider.getParseClaims(any())).willReturn(new AuthInfo(1L, "USER"));
    }

    @DisplayName("댓글을 작성할 수 있다.")
    @Test
    void writeComment() throws Exception {
        // given
        CommentRequest commentRequest = new CommentRequest("댓글 입니다.");
        ResultActions resultActions = mockMvc.perform(post("/boards/{postId}/comments", 1)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest))
        );

        // when
        resultActions.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(
                        "Post Comment",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postId")
                                        .description("게시판 ID")
                        ),
                        requestFields(
                                fieldWithPath("commentContent")
                                        .type(STRING)
                                        .description("작성할 댓글 내용")
                        )
                ));

        // then
        verify(commentService).postComment(anyLong(), anyLong(), any(CommentRequest.class));
    }
}