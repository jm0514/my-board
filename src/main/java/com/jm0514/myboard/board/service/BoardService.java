package com.jm0514.myboard.board.service;

import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponseDto writeBoard(final Long memberId, final BoardRequestDto request) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));
        Board writtenBoard = request.toEntity(request.getTitle(), request.getContent(), findMember);
        boardRepository.save(writtenBoard);
        return BoardResponseDto.of(writtenBoard);
    }

    public BoardResponseDto findBoard(final Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));
        return BoardResponseDto.of(findBoard);
    }

    @Transactional
    public void modifyBoard(final Long memberId,
                            final Long boardId,
                            final BoardRequestDto requestDto
    ) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));
        Member findWriter = findBoard.findWriter();
        if (!Objects.equals(findWriter.getId(), memberId)) {
            throw new BadRequestException(INVALID_WRITER_EXCEPTION);
        }

        findBoard.modifyBoard(requestDto.getTitle(), requestDto.getContent());
    }
}
