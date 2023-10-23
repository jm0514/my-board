package com.jm0514.myboard.like.service;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.like.domain.PostLike;
import com.jm0514.myboard.like.dto.PostLikeResponse;
import com.jm0514.myboard.like.repository.PostLikeRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jm0514.myboard.member.domain.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;

class PostLikeServiceTest extends IntegrationTestSupport {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("해당 게시글에 해당 회원이 좋아요를 한 적이 없는 경우, 좋아요를 반영한다.")
    @Test
    void postLike(){

        // given
        Member createdMember = getMember();
        Board createdBoard = getBoard(createdMember);
        memberRepository.save(createdMember);
        boardRepository.save(createdBoard);

        // when
        PostLikeResponse response = postLikeService
                .postLike(createdMember.getId(), createdBoard.getId());

        // then
        assertThat(response.isLike()).isEqualTo(true);
    }

    @DisplayName("해당 게시글에 해당 회원이 좋아요를 했을 경우, 좋아요를 취소한다.")
    @Test
    void postLikeCancel(){

        // given
        Member createdMember = getMember();
        Board createdBoard = getBoard(createdMember);
        PostLike postLike = getPostLike(createdMember, createdBoard);
        memberRepository.save(createdMember);
        boardRepository.save(createdBoard);
        postLikeRepository.save(postLike);

        // when
        PostLikeResponse response = postLikeService
                .postLike(createdMember.getId(), createdBoard.getId());

        // then
        assertThat(response.isLike()).isEqualTo(false);
    }

    private PostLike getPostLike(
            final Member createdMember,
            final Board createdBoard
    ) {
        return PostLike.builder()
                .member(createdMember)
                .board(createdBoard)
                .build();
    }

    private Board getBoard(final Member createdMember) {
        return Board.builder()
                .member(createdMember)
                .title("제목")
                .content("내용")
                .build();
    }

    private Member getMember() {
        return Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .profileImageUrl("URL")
                .roleType(USER)
                .build();
    }
}