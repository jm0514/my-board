package com.jm0514.myboard.like.facade;

import com.jm0514.myboard.like.dto.PostLikeResponse;
import com.jm0514.myboard.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OptimisticLockLikeFacade {

    private final PostLikeService postLikeService;

    public PostLikeResponse postLike(final Long memberId, final Long boardId) throws InterruptedException {
        while (true) {
            try {
                return postLikeService.postLike(memberId, boardId);
            } catch (ObjectOptimisticLockingFailureException e) {
                Thread.sleep(100);
            }
        }
    }
}
