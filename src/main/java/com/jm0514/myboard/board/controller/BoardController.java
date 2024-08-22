package com.jm0514.myboard.board.controller;

import com.jm0514.myboard.auth.Login;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardTotalInfoResponse;
import com.jm0514.myboard.board.service.BoardCacheService;
import com.jm0514.myboard.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final BoardService boardService;
    private final BoardCacheService boardCacheService;

    @PostMapping
    public ResponseEntity<BoardTotalInfoResponse> writeBoard(
            final @Login AuthInfo authInfo,
            final @RequestBody @Valid BoardRequestDto request
    ) {
        Long memberId = authInfo.getId();
        BoardTotalInfoResponse responseDto = boardService.writeBoard(memberId, request);
        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardTotalInfoResponse> findBoard(
            final @PathVariable Long boardId
    ) {
        BoardTotalInfoResponse findBoard = boardCacheService.findBoardCache(boardId);
        return ResponseEntity.status(OK).body(findBoard);
    }

    @GetMapping
    public ResponseEntity<List<BoardTotalInfoResponse>> findLimitedBoardList(final Pageable pageable) {
        List<BoardTotalInfoResponse> limitedBoardList = boardCacheService.findLimitedBoardListCache(pageable);
        return ResponseEntity.status(OK).body(limitedBoardList);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> modifyBoard(
            final @Login AuthInfo authInfo,
            final @PathVariable Long boardId,
            final @RequestBody @Valid BoardRequestDto requestDto
    ) {
        Long memberId = authInfo.getId();
        boardService.modifyBoard(memberId, boardId, requestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1")
    public ResponseEntity<List<BoardTotalInfoResponse>> findLimitedBoardList_v1(
            final @RequestParam(required = false) Long lastBoardId,
            final @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size
    ) {
        List<BoardTotalInfoResponse> limitedBoardList = boardCacheService.findLimitedBoardListCache_v1(lastBoardId, size);
        return ResponseEntity.status(OK).body(limitedBoardList);
    }
}
