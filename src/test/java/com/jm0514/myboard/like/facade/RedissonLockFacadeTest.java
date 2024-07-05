package com.jm0514.myboard.like.facade;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.board.repository.BoardRepository;
import com.jm0514.myboard.global.IntegrationTestSupport;
import com.jm0514.myboard.global.exception.BadRequestException;
import com.jm0514.myboard.like.repository.PostLikeRepository;
import com.jm0514.myboard.like.service.PostLikeService;
import com.jm0514.myboard.member.domain.Member;
import com.jm0514.myboard.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jm0514.myboard.global.exception.ExceptionStatus.NOT_FOUND_BOARD_EXCEPTION;
import static com.jm0514.myboard.member.domain.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;

class RedissonLockFacadeTest extends IntegrationTestSupport {

    public static final int N_THREADS = 4;

    @Autowired
    private RedissonLockFacade redissonLockFacade;

    @Autowired
    private PostLikeService postLikeService;

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

    @DisplayName("Redisson 분산 락을 이용하여 100명의 회원이 동시에 좋아요를 눌렀을 때, 총 좋아요 개수가 전부 반영된다.")
    @Test
    void redissonLockTest() throws InterruptedException {
        // given
        List<Member> members = Stream.generate(this::getMember).limit(100)
                .collect(Collectors.toList());

        Board createdBoard = getBoard(members.get(0));

        memberRepository.saveAll(members);
        boardRepository.save(createdBoard);

        int threadCount = members.size();
        CountDownLatch latch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

        // 회원 100명이 동시에 좋아요를 누르는 상황
        for (Member member : members) {
            executorService.execute(() -> {
                try {
                    redissonLockFacade.postLike(member.getId(), createdBoard.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // when
        Board board = boardRepository.findById(createdBoard.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));

        // then
        assertThat(board.getTotalLikeCount()).isEqualTo(100);
    }

    @DisplayName("락을 걸지 않았을 때, 동시에 100명의 회원이 좋아요를 눌렀다면 총 좋아요 개수가 100개가 아닙니다.")
    @Test
    void likeCountTest() throws InterruptedException {
        // given
        List<Member> members = Stream.generate(this::getMember).limit(100)
                .collect(Collectors.toList());

        Board createdBoard = getBoard(members.get(0));

        memberRepository.saveAll(members);
        boardRepository.save(createdBoard);

        int threadCount = members.size();
        CountDownLatch latch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

        // 회원 100명이 동시에 좋아요를 누르는 상황
        for (Member member : members) {
            executorService.execute(() -> {
                try {
                    postLikeService.postLike(member.getId(), createdBoard.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // when
        Board board = boardRepository.findById(createdBoard.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_EXCEPTION));

        // then
        assertThat(board.getTotalLikeCount()).isNotEqualTo(100);
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