package com.jm0514.myboard.board.controller;

import com.jm0514.myboard.auth.Login;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.board.dto.BoardRequestDto;
import com.jm0514.myboard.board.dto.BoardResponseDto;
import com.jm0514.myboard.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> writeBoard(
            final @Login AuthInfo authInfo,
            final @RequestBody @Valid BoardRequestDto request
    ) {
        Long memberId = authInfo.getId();
        BoardResponseDto responseDto = boardService.writeBoard(memberId, request);
        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findBoard(final @PathVariable Long boardId) {
        BoardResponseDto findBoard = boardService.findBoard(boardId);
        return ResponseEntity.status(OK).body(findBoard);
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
}
