package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.auth.domain.repository.RefreshTokenRedisRepository;
import com.jm0514.myboard.global.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jm0514.myboard.global.exception.ExceptionStatus.INVALID_REFRESH_TOKEN_EXCEPTION;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtProvider jwtProvider;

    public void saveToken(final String memberId, final String token) {
        RefreshToken refreshToken = new RefreshToken(memberId, token);
        refreshTokenRedisRepository.save(refreshToken);
    }

    public void match(final String memberId, final String refreshToken) {
        RefreshToken savedToken = refreshTokenRedisRepository
                .findByMemberId(memberId).get(0);

        if(!jwtProvider.validateRefreshToken(savedToken.getToken())) {
            refreshTokenRedisRepository.delete(savedToken);
            throw new AuthException(INVALID_REFRESH_TOKEN_EXCEPTION);
        }
        savedToken.validateSameToken(refreshToken);
    }

    public void deleteToken(final String memberId) {
        List<RefreshToken> refreshTokenList = refreshTokenRedisRepository.findByMemberId(memberId);
        if (!refreshTokenList.isEmpty()) {
            refreshTokenRedisRepository.deleteAll(refreshTokenList);
        }
    }
}
