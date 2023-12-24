package com.jm0514.myboard.like.contoller;

import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.global.IntegrationControllerTest;
import com.jm0514.myboard.like.dto.PostLikeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class PostLikeControllerTest extends IntegrationControllerTest {

    private final static String ACCESS_TOKEN = "Bearer accessToken";

    @BeforeEach
    void setUp() {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        given(jwtProvider.getParseClaims(any())).willReturn(new AuthInfo(1L, "USER"));
    }

    @DisplayName("좋아요를 누른 것이 반영된다.")
    @Test
    void postLike() throws Exception {
        // given
        PostLikeResponse response = new PostLikeResponse(true);

        given(optimisticLockLikeFacade.postLike(anyLong(), anyLong())).willReturn(response);
        ResultActions resultActions = mockMvc.perform(post("/boards/{boardId}/likes", 1)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response))
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(document(
                        "Push Like",
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
                        responseFields(
                                fieldWithPath("like")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("좋아요가 눌림")
                        ))
                )
                .andReturn();

        PostLikeResponse actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                PostLikeResponse.class
        );

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(response);
    }

}