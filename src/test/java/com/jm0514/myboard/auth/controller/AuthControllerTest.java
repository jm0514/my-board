package com.jm0514.myboard.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.domain.RefreshTokenService;
import com.jm0514.myboard.auth.dto.AccessTokenResponse;
import com.jm0514.myboard.auth.dto.LoginRequest;
import com.jm0514.myboard.auth.service.AuthService;
import com.jm0514.myboard.global.ControllerTest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class AuthControllerTest extends ControllerTest {

    private final static String GOOGLE_PROVIDER = "google";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @DisplayName("로그인을 할 수 있다.")
    @Test
    void login() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("code");
        MemberTokens memberTokens = new MemberTokens(GOOGLE_PROVIDER, loginRequest.getCode());

        given(authService.login(anyString(), anyString()))
                .willReturn(memberTokens);

        doAnswer(invocation -> null).when(refreshTokenService)
                .saveToken(anyLong(), anyString());

        ResultActions resultActions = mockMvc.perform(
                post("/login/{provider}", GOOGLE_PROVIDER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest))
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("provider")
                                        .description("로그인 유형")
                        ),
                        requestFields(
                                fieldWithPath("code")
                                        .type(JsonFieldType.STRING)
                                        .description("인가 코드")
                        ),
                        responseFields(
                                fieldWithPath("accessToken")
                                        .type(JsonFieldType.STRING)
                                        .description("access token")
                        )

                ))
                .andReturn();

        AccessTokenResponse expected = new AccessTokenResponse(memberTokens.getAccessToken());

        AccessTokenResponse actual = objectMapper.readValue(
                    mvcResult.getResponse().getContentAsString(),
                    AccessTokenResponse.class
        );

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}