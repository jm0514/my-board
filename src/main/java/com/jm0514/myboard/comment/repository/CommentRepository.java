package com.jm0514.myboard.comment.repository;

import com.jm0514.myboard.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
