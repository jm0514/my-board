package com.jm0514.myboard.auth.domain.repository;

import com.jm0514.myboard.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberId(final Long memberId);

    void deleteAllByMemberId(final Long memberId);
}
