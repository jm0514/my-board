package com.jm0514.myboard.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.domain.RefreshTokenService;
import com.jm0514.myboard.auth.dto.AccessTokenResponse;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.auth.dto.LoginRequest;
import com.jm0514.myboard.auth.service.AuthService;
import com.jm0514.myboard.global.ControllerTest;
import jakarta.servlet.http.Cookie;
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
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
    private final static String REFRESH_TOKEN = "refreshToken";
    private final static String ACCESS_TOKEN = "Bearer accessToken";

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

        doNothing().when(refreshTokenService)
                .saveToken(anyLong(), anyString());

        ResultActions resultActions = mockMvc.perform(
                post("/login/{provider}", GOOGLE_PROVIDER)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest))
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("Login",
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

    @DisplayName("리프레시 토큰을 통해 엑세스 토큰을 재발급 받을 수 있다.")
    @Test
    void refreshAccessTokenByRefreshToken() throws Exception {
        // given
        AuthInfo authInfo = new AuthInfo(1L, "USER");
        MemberTokens memberTokens = new MemberTokens(REFRESH_TOKEN, ACCESS_TOKEN);

        doNothing().when(refreshTokenService)
                .match(anyLong(), anyString());

        Cookie cookie = new Cookie("refresh-token", memberTokens.getRefreshToken());

        // TODO: accessToken 검증에서 왜 false가 나올까
        given(jwtProvider.validateAccessToken(anyString()))
                .willReturn(true);
        given(jwtProvider.getParseClaims(anyString()))
                .willReturn(authInfo);
        given(jwtProvider.generateLoginToken(authInfo))
                .willReturn(memberTokens);

        ResultActions resultActions = mockMvc.perform(get("/refresh")
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .cookie(cookie)
        );

        // when then
        resultActions.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(
                        "Refresh Token",
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
                        responseHeaders(
                                headerWithName("Authorization")
                                        .description("갱신된 엑세스 토큰")
                        )
                ));
    }
}