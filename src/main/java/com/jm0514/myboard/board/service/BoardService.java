package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.comment.dto.CommentResponse;
import com.jm0514.myboard.comment.service.CommentService;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    public static final int PAGE_SIZE = 10;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;

    @Transactional
    public BoardResponseDto writeBoard(final Long memberId, final BoardRequestDto request) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));
        Board writtenBoard = request.toEntity(request.getTitle(), request.getContent(), findMember);
        boardRepository.save(writtenBoard);
        return BoardResponseDto.of(writtenBoard);
    }

    public BoardTotalInfoResponse findBoard(final Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));
        List<CommentResponse> comments = commentService.getComments(findBoard);
        return BoardTotalInfoResponse.of(findBoard, comments);
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

    public List<BoardTotalInfoResponse> findLimitedBoardList() {
        List<BoardTotalInfoResponse> responseList = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Direction.DESC, ("createdAt")));
        List<Board> pagedList = boardRepository.findLimitedBoardList(pageRequest);
        for (Board board : pagedList) {
            List<CommentResponse> comments = commentService.getComments(board);
            responseList.add(BoardTotalInfoResponse.of(board, comments));
        }

        return responseList;
    }
}
