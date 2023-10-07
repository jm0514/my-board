package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.global.exception.AuthException;
import com.jm0514.myboard.auth.dto.AuthInfo;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.jm0514.myboard.global.exception.ExceptionStatus.EXPIRED_PERIOD_JWT_EXCEPTION;
import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_JWT_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class JwtProviderTest {

    private static final Long SAMPLE_EXPIRATION_TIME = 60000L;
    private static final Long SAMPLE_EXPIRED_TIME = 0L;
    private static final Long SAMPLE_ID = 1L;
    private static final String SAMPLE_ROLE = "USER";

    private static final String SAMPLE_SUBJECT = "sampleSubject";
    private static final String SAMPLE_INVALID_SECRET_KEY = "dkanakfdlskTJqhkttmselkdwaffelgkngEAglkawlkawMW";

    @Value("${security.jwt.secret-key}")
    private String realSecretKey;

    @Autowired
    JwtProvider jwtProvider;

    AuthInfo authInfo = new AuthInfo(SAMPLE_ID, SAMPLE_ROLE);

    private MemberTokens makeTestMemberTokens() {
        return jwtProvider.generateLoginToken(authInfo);
    }

    private String makeTestJwt(final Long expirationTime, final String secretKey) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(JwtProviderTest.SAMPLE_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(
                        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    @DisplayName("access token과 refresh token을 생성할 수 있다.")
    @Test
    void generateLoginToken() {
        // given
        MemberTokens memberTokens = makeTestMemberTokens();

        // when then
        assertThat(jwtProvider.getParseClaims(memberTokens.getAccessToken()).getId()).isEqualTo(SAMPLE_ID);
        assertThat(jwtProvider.getParseClaims(memberTokens.getAccessToken()).getRoleType()).isEqualTo(SAMPLE_ROLE);

        assertThat(jwtProvider.getParseClaims(memberTokens.getRefreshToken()).getId()).isNull();
        assertThat(jwtProvider.getParseClaims(memberTokens.getRefreshToken()).getRoleType()).isNull();
    }

    @DisplayName("access token이 유효한 토큰일 때 검증을 통과한다.")
    @Test
    void validateAccessToken_Success(){
        // given
        MemberTokens memberTokens = makeTestMemberTokens();

        // when then
        assertDoesNotThrow(() -> jwtProvider.validateAccessToken(memberTokens.getAccessToken()));
    }

    @DisplayName("refresh token이 유효한 토큰일 때 검증을 통과한다.")
    @Test
    void validateRefreshToken_Success(){
        // given
        MemberTokens memberTokens = makeTestMemberTokens();

        // when then
        assertDoesNotThrow(() -> jwtProvider.validateRefreshToken(memberTokens.getRefreshToken()));
    }

    @DisplayName("refresh token의 기한이 만료되었을 때 예외 처리한다.")
    @Test
    void refreshTokenExpired() {
        // given
        String refreshToken = makeTestJwt(SAMPLE_EXPIRED_TIME, realSecretKey);

        // when then
        assertThatThrownBy(() -> jwtProvider.validateRefreshToken(refreshToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_PERIOD_JWT_EXCEPTION.getMessage());
    }

    @DisplayName("access token의 기한이 만료되었을 때 예외 처리한다.")
    @Test
    void accessTokenExpired() {
        // given
        String accessToken = makeTestJwt(SAMPLE_EXPIRED_TIME, realSecretKey);

        // when then
        assertThatThrownBy(() -> jwtProvider.validateAccessToken(accessToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_PERIOD_JWT_EXCEPTION.getMessage());
    }

    @DisplayName("refresh token의 형식이 올바르지 않을 때 예외 처리한다.")
    @Test
    void invalidRefreshToken(){
        // given
        String refreshToken =  makeTestJwt(SAMPLE_EXPIRATION_TIME, SAMPLE_INVALID_SECRET_KEY);

        // when then
        assertThatThrownBy(() -> jwtProvider.validateRefreshToken(refreshToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_JWT_EXCEPTION.getMessage());
    }

    @DisplayName("access token의 형식이 올바르지 않을 때 예외 처리한다.")
    @Test
    void invalidAccessToken(){
        // given
        String accessToken =  makeTestJwt(SAMPLE_EXPIRATION_TIME, SAMPLE_INVALID_SECRET_KEY);

        // when then
        assertThatThrownBy(() -> jwtProvider.validateAccessToken(accessToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_JWT_EXCEPTION.getMessage());
    }
}