package com.jm0514.myboard.like.contoller;

import com.jm0514.myboard.auth.Login;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.like.dto.PostLikeResponse;
import com.jm0514.myboard.like.facade.OptimisticLockLikeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final OptimisticLockLikeFacade optimisticLockLikeFacade;

    @PostMapping("/boards/{boardId}/likes")
    public ResponseEntity<PostLikeResponse> postLike(
            final @Login AuthInfo authInfo,
            final @PathVariable Long boardId
    ) throws InterruptedException {
        PostLikeResponse response = optimisticLockLikeFacade.postLike(authInfo.getId(), boardId);
        return ResponseEntity.status(CREATED).body(response);
    }
}
