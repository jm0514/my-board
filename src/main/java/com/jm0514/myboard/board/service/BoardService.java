package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.exception.InvalidWriterException;
import com.jm0514.myboard.board.exception.NotFoundBoardException;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.exception.NotFoundMemberException;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponseDto writeBoard(final Long memberId, final BoardRequestDto request) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        Board writtenBoard = request.toEntity(request.getTitle(), request.getContent(), findMember);
        boardRepository.save(writtenBoard);
        return BoardResponseDto.of(writtenBoard);
    }

    public BoardResponseDto findBoard(final Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(NotFoundBoardException::new);
        return BoardResponseDto.of(findBoard);
    }

    @Transactional
    public void modifyBoard(final Long memberId,
                            final Long boardId,
                            final BoardRequestDto requestDto
    ) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        Member findWriter = findBoard.findWriter();
        if (!Objects.equals(findWriter.getId(), memberId)) {
            throw new InvalidWriterException();
        }

        findBoard.modifyBoard(requestDto.getTitle(), requestDto.getContent());
    }
}
