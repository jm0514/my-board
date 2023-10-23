package com.jm0514.myboard.like.repository;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.like.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByBoardAndMemberId(Board board, Long memberId);

    PostLike findByBoardAndMemberId(Board board, Long memberId);
}
