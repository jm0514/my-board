package com.jm0514.myboard.comment.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.comment.dto.CommentRequest;
import com.jm0514.myboard.comment.repository.CommentRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jm0514.myboard.member.domain.RoleType.USER;

class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
    }

    @DisplayName("댓글 내용을 성공적으로 달 수 있다.")
    @Test
    void postComment() {
        // given
        CommentRequest request = new CommentRequest("댓글 내용입니다.");

        Member createdMember = Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .roleType(USER)
                .profileImageUrl("URL")
                .build();
        memberRepository.save(createdMember);
        Board createdBoard = Board.builder()
                .title("제목")
                .content("내용입니다.")
                .member(createdMember)
                .build();
        boardRepository.save(createdBoard);

        Comment comment = Comment.of(request.getCommentContent(), createdBoard, createdMember);
        commentRepository.save(comment);

        // then when
        commentService.postComment(createdMember.getId(), createdBoard.getId(), request);
    }
}