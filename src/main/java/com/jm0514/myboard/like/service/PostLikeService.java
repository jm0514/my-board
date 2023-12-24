package com.jm0514.myboard.like.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.like.domain.PostLike;
import com.jm0514.myboard.like.dto.PostLikeResponse;
import com.jm0514.myboard.like.repository.PostLikeRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_BOARD_EXCEPTION;
import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_MEMBER_EXCEPTION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // TODO: 메서드 쪼개기
    @Transactional
    public PostLikeResponse postLike(final Long memberId, final Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));

        boolean existsLike = postLikeRepository.existsByBoardAndMemberId(findBoard, memberId);

        if (existsLike) {
            PostLike findPostLike = postLikeRepository.findByBoardAndMemberId(findBoard, memberId);
            postLikeRepository.delete(findPostLike);

            findBoard.likeDown();

            return new PostLikeResponse(false);
        }

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));

        PostLike postLike = PostLike.builder()
                .board(findBoard)
                .member(findMember)
                .build();
        postLikeRepository.saveAndFlush(postLike);

        findBoard.likeUp();

        return new PostLikeResponse(true);
    }
}
