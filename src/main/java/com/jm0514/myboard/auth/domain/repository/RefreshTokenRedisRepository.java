package com.jm0514.myboard.auth.domain.repository;

import com.jm0514.myboard.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    List<RefreshToken> findByMemberId(final String memberId);
}
