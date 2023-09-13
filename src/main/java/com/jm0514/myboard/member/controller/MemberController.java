package com.jm0514.myboard.member.controller;

import com.jm0514.myboard.auth.Login;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.member.dto.MemberInfoRequestDto;
import com.jm0514.myboard.member.dto.MemberInfoResponseDto;
import com.jm0514.myboard.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> findMemberInfo(@Login final AuthInfo authInfo) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(authInfo);
        return ResponseEntity.ok(memberInfoResponseDto);
    }

    @PatchMapping("/info")
    public ResponseEntity<Void> updateMemberInfo(
            @Login final AuthInfo authInfo,
            @RequestBody @Valid final MemberInfoRequestDto request
    ) {
        memberService.updateMember(authInfo, request);
        return ResponseEntity.noContent().build();
    }

}
