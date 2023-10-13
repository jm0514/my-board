package com.jm0514.myboard.comment.repository;

import com.jm0514.myboard.board.domain.Board;
import com.jm0514.myboard.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentByBoard(Board board);
}
