package com.annb.quizz.repository;

import com.annb.quizz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    Long countByQuizz_Id(String id);
    List<Question> findByQuizzId(String quizzId);
}
