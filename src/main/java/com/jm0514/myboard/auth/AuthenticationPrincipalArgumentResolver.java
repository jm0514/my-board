package com.jm0514.myboard.auth;

import com.jm0514.myboard.advice.AuthException;
import com.jm0514.myboard.auth.domain.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_TOKEN_EXCEPTION;

@RequiredArgsConstructor
@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = AuthorizationExtractor.extract(Objects.requireNonNull(request));
        if (!jwtProvider.validateAccessToken(accessToken)) {
            throw new AuthException(INVALID_TOKEN_EXCEPTION);
        }
        return jwtProvider.getParseClaims(accessToken);
    }
}
