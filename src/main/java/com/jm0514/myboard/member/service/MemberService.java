package com.jm0514.myboard.member.service;

import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.dto.MemberInfoRequestDto;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.exception.NotFoundMemberException;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponseDto findMemberInfo(final AuthInfo authInfo) {
        Member findMember = getMember(authInfo);
        return new MemberInfoResponseDto(findMember.getName(), findMember.getProfileImageUrl());
    }

    @Transactional
    public void updateMember(
            final AuthInfo authInfo,
            final MemberInfoRequestDto memberInfoRequestDto
    ) {
        Member findMember = getMember(authInfo);
        findMember.updateMember(memberInfoRequestDto.getName(), memberInfoRequestDto.getProfileImageUrl());
    }

    private Member getMember(final AuthInfo authInfo) {
        return memberRepository.findById(authInfo.getId())
                .orElseThrow(NotFoundMemberException::new);
    }
}
