package com.jm0514.myboard.comment.repository;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.comment.domain.Comment;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.domain.RoleType;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;


    @DisplayName("작성한 댓글들 목록을 조회한다.")
    @Test
    void findCommentByBoard(){
        // given
        Member createdMember1 = Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .roleType(RoleType.USER)
                .profileImageUrl("이미지 Url")
                .build();
        Member createdMember2 = Member.builder()
                .loginAccountId("4321")
                .name("민정")
                .roleType(RoleType.USER)
                .profileImageUrl("이미지 Url")
                .build();
        Board createdBoard = Board.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(createdMember1)
                .build();
        Comment comment1 = Comment.builder()
                .commentContent("댓글 내용입니다1.")
                .member(createdMember2)
                .board(createdBoard)
                .build();
        Comment comment2 = Comment.builder()
                .commentContent("댓글 내용입니다2.")
                .member(createdMember1)
                .board(createdBoard)
                .build();

        boardRepository.save(createdBoard);
        memberRepository.saveAll(List.of(createdMember1, createdMember2));
        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        List<Comment> comments = commentRepository.findCommentByBoard(createdBoard);
        String content1 = comments.get(0).getValidatedComment();
        String content2 = comments.get(1).getValidatedComment();

        // then
        assertThat(comments).hasSize(2);
        Assertions.assertEquals(content1, "댓글 내용입니다1.");
        Assertions.assertEquals(content2, "댓글 내용입니다2.");
    }
}