package com.jm0514.myboard.auth;

import com.jm0514.myboard.global.exception.AuthException;
import com.jm0514.myboard.global.exception.ExceptionStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_TOKEN_EXCEPTION;

public class AuthorizationExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public static String extract(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorizationHeader)) {
            throw new AuthException(ExceptionStatus.EMPTY_HEADER_EXCEPTION);
        }

        validateAuthorizationFormat(authorizationHeader);
        return authorizationHeader.substring(BEARER_TYPE.length()).trim();
    }

    private static void validateAuthorizationFormat(final String authorizationHeader) {
        if (!authorizationHeader.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new AuthException(INVALID_TOKEN_EXCEPTION);
        }
    }
}
