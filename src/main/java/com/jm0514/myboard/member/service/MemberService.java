package com.jm0514.myboard.member.service;

import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.dto.MemberInfoRequestDto;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_MEMBER_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponseDto findMemberInfo(final AuthInfo authInfo) {
        Member findMember = getMember(authInfo);
        String getNickname = findMember.getName();
        String getProfileImageUrl = findMember.getProfileImageUrl();
        LocalDateTime createdAt = findMember.getCreatedAt();
        return MemberInfoResponseDto.of(getNickname, getProfileImageUrl, createdAt);
    }

    @Transactional
    public void updateMember(
            final AuthInfo authInfo,
            final MemberInfoRequestDto memberInfoRequestDto
    ) {
        Member findMember = getMember(authInfo);
        findMember.modifyMember(memberInfoRequestDto.getName(), memberInfoRequestDto.getProfileImageUrl());
    }

    private Member getMember(final AuthInfo authInfo) {
        return memberRepository.findById(authInfo.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));
    }
}
