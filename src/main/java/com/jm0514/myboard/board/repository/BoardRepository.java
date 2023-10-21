package com.jm0514.myboard.board.repository;

import com.jm0514.myboard.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Transactional
    @Modifying
    @Query("update Board b " +
            "set b.totalLikeCount = b.totalLikeCount - 1 " +
            "where b.id = :boardId")
    void decreaseTotalLikeCount(@Param(value = "boardId") Long boardId);

    @Transactional
    @Modifying
    @Query("update Board b " +
            "set b.totalLikeCount = b.totalLikeCount + 1 " +
            "where b.id = :boardId")
    void increaseTotalLikeCount(@Param(value = "boardId") Long boardId);
}
