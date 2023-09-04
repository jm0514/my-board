package com.jm0514.myboard.auth.domain;

import com.jm0514.myboard.auth.domain.repository.RefreshTokenRepository;
import com.jm0514.myboard.auth.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(InvalidRefreshTokenException::new);

        if(!jwtProvider.validateRefreshToken(savedToken.toString())) {
            refreshTokenRepository.delete(savedToken);
            throw new InvalidRefreshTokenException();
        }
        savedToken.validateSameToken(refreshToken);
    }

    @Transactional
    public void deleteToken(final Long memberId) {
        refreshTokenRepository.deleteAllByMemberId(memberId);
    }
}
