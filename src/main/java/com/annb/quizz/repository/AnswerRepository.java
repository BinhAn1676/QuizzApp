package com.annb.quizz.repository;

import com.annb.quizz.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {
    List<Answer> findByQuestionId(String questionId);
}
