package com.jm0514.myboard.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm0514.myboard.auth.AuthenticationPrincipalArgumentResolver;
import com.jm0514.myboard.auth.AuthorizationExtractor;
import com.jm0514.myboard.auth.controller.AuthController;
import com.jm0514.myboard.auth.domain.JwtProvider;
import com.jm0514.myboard.auth.domain.RefreshTokenService;
import com.jm0514.myboard.auth.service.AuthService;
import com.jm0514.myboard.board.controller.BoardController;
import com.jm0514.myboard.board.service.BoardService;
import com.jm0514.myboard.comment.controller.CommentController;
import com.jm0514.myboard.comment.service.CommentService;
import com.jm0514.myboard.like.contoller.PostLikeController;
import com.jm0514.myboard.like.service.PostLikeService;
import com.jm0514.myboard.member.controller.MemberController;
import com.jm0514.myboard.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = {
        AuthController.class,
        BoardController.class,
        CommentController.class,
        PostLikeController.class,
        MemberController.class
})
public abstract class IntegrationControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    AuthorizationExtractor authorizationExtractor;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected RefreshTokenService refreshTokenService;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected PostLikeService postLikeService;

    @MockBean
    protected MemberService memberService;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider restDocumentation
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
