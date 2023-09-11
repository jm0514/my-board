package com.jm0514.myboard.member.repository;

import com.jm0514.myboard.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginAccountId(final String loginAccountId);
}
