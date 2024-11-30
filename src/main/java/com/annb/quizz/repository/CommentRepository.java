package com.annb.quizz.repository;

import com.annb.quizz.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByQuizzIdAndParentCommentIsNull(String quizzId); // Fetch top-level comments
    List<Comment> findByParentCommentId(String parentCommentId); // Fetch replies
}
