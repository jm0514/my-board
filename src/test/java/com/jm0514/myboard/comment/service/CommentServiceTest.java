package com.jm0514.myboard.comment.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.comment.dto.CommentRequest;
import com.jm0514.myboard.comment.dto.CommentResponse;
import com.jm0514.myboard.comment.repository.CommentRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.jm0514.myboard.member.domain.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("댓글 내용을 성공적으로 달 수 있다.")
    @Test
    void postComment() {
        // given
        CommentRequest request = new CommentRequest("댓글 내용입니다.");

        Member createdMember = getMember();
        memberRepository.save(createdMember);
        Board createdBoard = getBoard(createdMember);
        boardRepository.save(createdBoard);

        Comment comment = Comment.of(request.getCommentContent(), createdBoard, createdMember);
        commentRepository.save(comment);

        // then when
        commentService.postComment(createdMember.getId(), createdBoard.getId(), request);
    }

    @DisplayName("댓글 목록을 조회합니다.")
    @Test
    void getComments(){
        // given
        Member createdMember = getMember();
        memberRepository.save(createdMember);
        Board createdBoard = getBoard(createdMember);
        boardRepository.save(createdBoard);
        Comment comment1 = Comment.builder()
                .commentContent("댓글 내용입니다.1")
                .member(createdMember)
                .board(createdBoard)
                .build();
        Comment comment2 = Comment.builder()
                .commentContent("댓글 내용입니다.2")
                .member(createdMember)
                .board(createdBoard)
                .build();
        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        List<CommentResponse> comments = commentService.getComments(createdBoard);
        String content1 = comments.get(0).getContent();
        String content2 = comments.get(1).getContent();

        // then
        assertThat(comments).hasSize(2);
        assertEquals(content1, "댓글 내용입니다.1");
        assertEquals(content2, "댓글 내용입니다.2");
    }

    private Board getBoard(Member createdMember) {
        return Board.builder()
                .title("제목")
                .content("내용입니다.")
                .member(createdMember)
                .build();
    }

    private Member getMember() {
        return Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .roleType(USER)
                .profileImageUrl("URL")
                .build();
    }
}