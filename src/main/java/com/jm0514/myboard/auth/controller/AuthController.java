package com.jm0514.myboard.auth.controller;

import com.jm0514.myboard.auth.Login;
import com.jm0514.myboard.auth.domain.JwtProvider;
import com.jm0514.myboard.auth.domain.MemberTokens;
import com.jm0514.myboard.auth.domain.RefreshTokenService;
import com.jm0514.myboard.auth.dto.AccessTokenResponse;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.auth.dto.LoginRequest;
import com.jm0514.myboard.auth.exception.EmptyHeaderException;
import com.jm0514.myboard.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AuthController {

    public static final int COOKIE_AGE_SECONDS = 604800;

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login/{provider}")
    public ResponseEntity<AccessTokenResponse> login(
            @PathVariable final String provider,
            @RequestBody final LoginRequest loginRequest,
            final HttpServletResponse response
    ) {
        MemberTokens memberTokens = authService.login(provider, loginRequest.getCode());
        ResponseCookie cookie = ResponseCookie.from("refresh-token", memberTokens.getRefreshToken())
                .maxAge(COOKIE_AGE_SECONDS)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(SET_COOKIE, cookie.toString());
        return ResponseEntity.status(CREATED).body(new AccessTokenResponse(memberTokens.getAccessToken()));
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(
            final @Login AuthInfo authInfo,
            final @CookieValue(value = "refresh-token") String refreshToken,
            final HttpServletRequest request
    ) {
        validateExistHeader(request);

        Long memberId = authInfo.getId();
        refreshTokenService.match(memberId, refreshToken);

        String accessToken = jwtProvider.generateLoginToken(authInfo).getAccessToken();

        return ResponseEntity.noContent()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
    }

    private void validateExistHeader(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshTokenHeader = request.getHeader(HttpHeaders.COOKIE);
        if (Objects.isNull(authorizationHeader) || Objects.isNull(refreshTokenHeader)) {
            throw new EmptyHeaderException();
        }
    }
}
