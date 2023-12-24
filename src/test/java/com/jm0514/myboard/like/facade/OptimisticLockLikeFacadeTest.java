package com.jm0514.myboard.like.facade;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.like.repository.PostLikeRepository;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_BOARD_EXCEPTION;
import static com.jm0514.myboard.member.domain.RoleType.USER;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OptimisticLockLikeFacadeTest {

    @Autowired
    private OptimisticLockLikeFacade optimisticLockLikeFacade;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    public void clear() {
        postLikeRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("낙관적 락을 통해 좋아요 기능 동시성 문제를 해결한다.")
    @Test
    void optimisticLockTest() throws InterruptedException {
        // given
        Member member1 = getMember();
        Member member2 = getMember();
        Member member3 = getMember();
        Member member4 = getMember();
        Board createdBoard = getBoard(member1);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        boardRepository.save(createdBoard);

        int threadCount = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 회원 4명이 동시에 좋아요를 누르는 상황
        executorService.execute(() -> {
            try {
                optimisticLockLikeFacade.postLike(member1.getId(), createdBoard.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        executorService.execute(() -> {
            try {
                optimisticLockLikeFacade.postLike(member2.getId(), createdBoard.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        executorService.execute(() -> {
            try {
                optimisticLockLikeFacade.postLike(member3.getId(), createdBoard.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                optimisticLockLikeFacade.postLike(member4.getId(), createdBoard.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        //when
        Board board = boardRepository.findById(createdBoard.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));

        // then
        assertThat(board.getTotalLikeCount()).isEqualTo(4);
    }

    private Member getMember() {
        return Member.builder()
                .loginAccountId("1234")
                .name("정민")
                .profileImageUrl("URL")
                .roleType(USER)
                .build();
    }

    private Board getBoard(final Member createdMember) {
        return Board.builder()
                .member(createdMember)
                .title("제목")
                .content("내용")
                .build();
    }

}