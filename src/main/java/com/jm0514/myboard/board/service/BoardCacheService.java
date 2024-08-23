package com.jm0514.myboard.board.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.board.repository.BoardQueryRepository;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_BOARD_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardCacheService {

    private final BoardQueryRepository boardQueryRepository;
    private final BoardRepository boardRepository;

    @Cacheable(value = "boardCache", key = "#boardId", cacheManager = "rcm")
    public BoardTotalInfoResponse findBoardCache(final Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));
        return BoardTotalInfoResponse.of(findBoard, findBoard.getCommentList());
    }

    @Cacheable(value = "boardListCache", key = "{#pageable.pageNumber, #pageable.pageSize}", cacheManager = "rcm")
    public List<BoardTotalInfoResponse> findLimitedBoardListCache(Pageable pageable) {
        return boardRepository.findBoardsJoinCommentsAndMembers(pageable)
                .stream()
                .map(board -> BoardTotalInfoResponse.of(board, board.getCommentList()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "boardListCache", key = "{#lastBoardId, #size}", cacheManager = "rcm")
    public List<BoardTotalInfoResponse> findLimitedBoardListCache_v1(final Long lastBoardId, final int size) {
        return boardQueryRepository.findBoardTotalInfo(lastBoardId, size);
    }

    @CacheEvict(value = "boardListCache", allEntries = true, cacheManager = "rcm")
    public void clearBoardListCache() {}
}
