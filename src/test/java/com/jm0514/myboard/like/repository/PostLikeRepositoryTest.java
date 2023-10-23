package com.jm0514.myboard.like.repository;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.like.domain.PostLike;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.jm0514.myboard.member.domain.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PostLikeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("해당 게시글 정보와 해당 회원의 정보가 담긴 좋아요 컬럼이 존재한다.")
    @Test
    void existsByBoardAndMemberId(){
        // given
        Member createdMember = getMember();
        Board createdBoard = getBoard(createdMember);
        PostLike postLike = getPostLike(createdMember, createdBoard);
        memberRepository.save(createdMember);
        boardRepository.save(createdBoard);
        postLikeRepository.save(postLike);

        // when
        Boolean result = postLikeRepository
                .existsByBoardAndMemberId(createdBoard, createdMember.getId());

        // then
        assertThat(result).isEqualTo(true);

    }

    @DisplayName("해당 게시글 정보와 해당 회원의 정보가 담긴 좋아요 컬럼을 조회한다.")
    @Test
    void findByBoardAndMemberId(){
        // given
        Member createdMember = getMember();
        Board createdBoard = getBoard(createdMember);
        PostLike postLike = getPostLike(createdMember, createdBoard);
        memberRepository.save(createdMember);
        boardRepository.save(createdBoard);
        postLikeRepository.save(postLike);

        // when
        PostLike result = postLikeRepository
                .findByBoardAndMemberId(createdBoard, createdMember.getId());

        // then
        assertThat(result).isEqualTo(postLike);

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