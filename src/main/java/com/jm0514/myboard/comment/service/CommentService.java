package com.jm0514.myboard.comment.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.comment.dto.CommentRequest;
import com.jm0514.myboard.comment.repository.CommentRepository;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_BOARD_EXCEPTION;
import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_MEMBER_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void postComment(final Long memberId,
                            final Long postId,
                            final CommentRequest commentRequest
    ) {
        Board findBoard = boardRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_EXCEPTION));
        Comment comment = Comment.of(commentRequest.getCommentContent(), findBoard, findMember);
        commentRepository.save(comment);
    }
}
