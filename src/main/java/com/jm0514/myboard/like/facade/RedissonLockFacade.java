package com.jm0514.myboard.like.facade;

import com.jm0514.myboard.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockFacade {

    public static final int WAIT_TIME = 5;
    public static final int LEASE_TIME = 3;

    private final PostLikeService postLikeService;
    private final RedissonClient redissonClient;

    public void postLike(final Long memberId, final Long boardId) {
        RLock lock = redissonClient.getLock(boardId.toString());

        try {
            boolean available = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);

            if (!available) {
                log.info("lock 획득 실패");
                return;
            }

            postLikeService.postLike(memberId, boardId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
