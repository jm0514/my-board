package com.jm0514.myboard.member.controller;

import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.global.ControllerTest;
import com.jm0514.myboard.member.dto.MemberInfoRequestDto;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MemberControllerTest extends ControllerTest {

    private final static String ACCESS_TOKEN = "Bearer accessToken";
    private final static String NICKNAME = "jeong-min";
    private final static String PROFILE_IMAGE_URL = "https://jeong-min.jpg";
    private final static String UPDATED_NICKNAME = "min";
    private final static String UPDATED_PROFILE_IMAGE_URL = "https://min-jeong.jpg";

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        given(jwtProvider.getParseClaims(any())).willReturn(new AuthInfo(1L, "USER"));
    }

    @DisplayName("로그인한 사용자의 정보를 확인할 수 있다.")
    @Test
    void checkMemberInfo() throws Exception {
        // given
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(
                NICKNAME,
                PROFILE_IMAGE_URL,
                LocalDateTime.of(2023, 9, 16, 19, 32)
        );

        given(memberService.findMemberInfo(any(AuthInfo.class)))
                .willReturn(memberInfoResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/members/info")
                .header(AUTHORIZATION, ACCESS_TOKEN)
        );

        // when
        MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document(
                                "Check Member-Info",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("name")
                                        .type(STRING)
                                        .description("회원 닉네임"),
                                fieldWithPath("profileImageUrl")
                                        .type(STRING)
                                        .description("회원 프로필 사진 URL"),
                                fieldWithPath("createdAt")
                                        .type(STRING)
                                        .description("회원이 생성된 시간")
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

    @DisplayName("로그인한 사용자의 회원 정보를 수정할 수 있다.")
    @Test
    void updateLoginMemberInfo() throws Exception {
        // given
        MemberInfoRequestDto request = new MemberInfoRequestDto(UPDATED_NICKNAME, UPDATED_PROFILE_IMAGE_URL);

        doNothing().when(memberService).updateMember(any(AuthInfo.class), any(MemberInfoRequestDto.class));

        ResultActions resultActions = mockMvc.perform(patch("/members/info")
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // when
        resultActions.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document(
                                "Update Member-Info",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name")
                                        .type(STRING)
                                        .description("회원 닉네임"),
                                fieldWithPath("profileImageUrl")
                                        .type(STRING)
                                        .description("회원 프로필 사진 URL")
                        )
                ));

        // then
        verify(memberService).updateMember(any(AuthInfo.class), any(MemberInfoRequestDto.class));
    }
}