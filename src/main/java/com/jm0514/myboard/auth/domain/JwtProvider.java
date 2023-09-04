package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.auth.exception.ExpiredPeriodJwtException;
import com.jm0514.myboard.auth.exception.InvalidJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;

    public JwtProvider(
            @Value("${security.jwt.secret-key}") final String secretKey,
            @Value("${security.jwt.access-expiration-time}") final Long accessExpirationTime,
            @Value("${security.jwt.refresh-expiration-time}") final Long refreshExpirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public MemberTokens generateLoginToken(final AuthInfo authInfo) {
        String accessToken = createAccessToken(authInfo);
        String refreshToken = createRefreshToken();
        return new MemberTokens(refreshToken, accessToken);
    }

    private String createAccessToken(final AuthInfo authInfo) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("id", authInfo.getId())
                .claim("role", authInfo.getRoleType())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateRefreshToken(final String refreshToken) {
        try {
            return !parseToken(refreshToken).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new ExpiredPeriodJwtException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException();
        }
    }

    public boolean validateAccessToken(final String accessToken) {
        try {
            return !parseToken(accessToken).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new ExpiredPeriodJwtException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException();
        }
    }

    private Jws<Claims> parseToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    public AuthInfo getParseClaims(String token) {
        Claims claims;
        try {
            claims = parseToken(token).getBody();
        } catch (ExpiredJwtException ex) {
            Long id = ex.getClaims().get("id", Long.class);
            String role = ex.getClaims().get("role", String.class);
            return new AuthInfo(id, role);
        }

        Long id = claims.get("id", Long.class);
        String role = claims.get("role", String.class);
        return new AuthInfo(id, role);
    }
}
