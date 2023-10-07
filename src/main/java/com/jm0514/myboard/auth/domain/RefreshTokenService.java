package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.global.exception.AuthException;
import com.jm0514.myboard.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_REFRESH_TOKEN_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void saveToken(final Long memberId, final String token) {
        deleteToken(memberId);
        RefreshToken refreshToken = new RefreshToken(memberId, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void match(final Long memberId, final String refreshToken) {
        RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthException(INVALID_REFRESH_TOKEN_EXCEPTION));

        if(!jwtProvider.validateRefreshToken(savedToken.getToken())) {
            refreshTokenRepository.delete(savedToken);
            throw new AuthException(INVALID_REFRESH_TOKEN_EXCEPTION);
        }
        savedToken.validateSameToken(refreshToken);
    }

    @Transactional
    public void deleteToken(final Long memberId) {
        refreshTokenRepository.deleteAllByMemberId(memberId);
    }
}
