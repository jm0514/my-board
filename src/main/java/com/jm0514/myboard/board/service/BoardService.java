package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jm0514.myboard.global.exception.ExceptionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    public static final long LAST_BOARD_ID = 0L;
    public static final int PAGE_SIZE = 10;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardCacheService boardCacheService;

    @Transactional
    @CachePut(value = "boardCache", key = "#result.boardId", cacheManager = "rcm")
    public BoardTotalInfoResponse writeBoard(final Long memberId, final BoardRequestDto request) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));
        Board writtenBoard = request.toEntity(request.getTitle(), request.getContent(), findMember);

        try {
            boardRepository.save(writtenBoard);
            boardCacheService.findLimitedBoardListCache_v1(LAST_BOARD_ID, PAGE_SIZE);
        } catch (Exception e) {
            boardCacheService.clearBoardListCache();

            throw new RuntimeException(INTERNAL_SERVER_ERROR.getMessage());
        }

        return BoardTotalInfoResponse.writeBoard(writtenBoard);
    }

    public BoardTotalInfoResponse findBoard(final Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));
        return BoardTotalInfoResponse.of(findBoard, findBoard.getCommentList());
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

    public List<BoardTotalInfoResponse> findLimitedBoardList(Pageable pageable) {
        return boardRepository.findBoardsJoinCommentsAndMembers(pageable)
                .stream()
                .map(board -> BoardTotalInfoResponse.of(board, board.getCommentList()))
                .collect(Collectors.toList());
    }
}
