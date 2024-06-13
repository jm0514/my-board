package com.jm0514.myboard.board.repository;

import com.jm0514.myboard.board.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b " +
            "from Board b " +
            "join b.member " +
            "left join b.commentList c " +
            "left join c.member m " +
            "order by b.createdAt desc")
    List<Board> findBoardsJoinCommentsAndMembers(Pageable pageable);
}
