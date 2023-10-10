package com.jm0514.myboard.comment.controller;

import com.jm0514.myboard.auth.Login;
import com.jm0514.myboard.auth.dto.AuthInfo;
import com.jm0514.myboard.comment.dto.CommentRequest;
import com.jm0514.myboard.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/boards/{postId}/comments")
    public ResponseEntity<Void> postComments(
            final @Login AuthInfo authInfo,
            final @PathVariable Long postId,
            final @RequestBody @Valid CommentRequest request
    ) {
        commentService.postComment(authInfo, postId, request);
        return ResponseEntity.noContent().build();
    }
}
