package com.jm0514.myboard.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.global.ControllerTest;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MemberControllerTest extends ControllerTest {

    private final static String REFRESH_TOKEN = "refreshToken";
    private final static String ACCESS_TOKEN = "Bearer accessToken";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        given(jwtProvider.getParseClaims(any())).willReturn(new AuthInfo(1L, "USER"));
    }

    @DisplayName("로그인한 사용자의 정보를 확인할 수 있다.")
    @Test
    void checkMemberInfo() throws Exception{
        // given
        MemberTokens memberTokens = new MemberTokens(REFRESH_TOKEN, ACCESS_TOKEN);
        Cookie cookie = new Cookie("refresh-token", memberTokens.getRefreshToken());
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto("jeong-min", "https://newsimg.sedaily.com/2023/07/19/29S6XZABI3_1.jpg");

        given(memberService.findMemberInfo(any(AuthInfo.class)))
                .willReturn(memberInfoResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/members/info")
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .cookie(cookie)
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                                "check-member-info",
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
                        responseFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 닉네임"),
                                fieldWithPath("profileImageUrl")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 프로필 사진 URL")
                        )
                ))
                .andReturn();

        MemberInfoResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                MemberInfoResponseDto.class
        );

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(memberInfoResponseDto);
    }
}