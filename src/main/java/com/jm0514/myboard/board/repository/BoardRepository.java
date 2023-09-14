package com.jm0514.myboard.board.repository;

import com.jm0514.myboard.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
