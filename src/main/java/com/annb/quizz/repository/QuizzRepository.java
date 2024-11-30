package com.annb.quizz.repository;

import com.annb.quizz.dto.QuizSuggestionDTO;
import com.annb.quizz.dto.response.QuizSuggestionInterface;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, String> {

    @Query(value = "SELECT q.* FROM quizz q " +
            "JOIN topic t ON q.topic_id = t.id " +
            "WHERE (:textSearch IS NULL OR q.description LIKE CONCAT('%', :textSearch, '%') " +
            "OR q.title LIKE CONCAT('%', :textSearch, '%') " +
            "OR q.created_by LIKE CONCAT('%', :textSearch, '%') " +
            "OR t.code LIKE CONCAT('%', :textSearch, '%') " +
            "OR t.title LIKE CONCAT('%', :textSearch, '%') " +
            "OR t.description LIKE CONCAT('%', :textSearch, '%')) " +
            "AND (:status IS NULL OR q.status = :status) " +
            "AND (:from IS NULL OR DATE(q.created_at) >= DATE(:from)) " +
            "AND (:to IS NULL OR DATE(q.created_at) <= DATE(:to))",
            nativeQuery = true)
    Page<Quizz> findFiltered(@Param("textSearch") String textSearch,
                             @Param("from") LocalDateTime from,
                             @Param("to") LocalDateTime to,
                             @Param("status") Integer status,
                             Pageable pageable);


    @Query(value = """
        SELECT 
            q.id,
            q.title,
            q.description,
            q.status,
            q.topic_id,
            IFNULL(AVG(r.rating), 0) averageRating,
            IFNULL(COUNT(c.id), 0) totalComments
        FROM 
            quizz q
        LEFT JOIN 
            review r ON q.id = r.quizz_id
        LEFT JOIN 
            comment c ON c.quizz_id = q.id
        GROUP BY 
            q.id, q.title, q.description, q.status, q.topic_id
        ORDER BY 
            AVG(r.rating) DESC, COUNT(c.id) DESC
        """,
            countQuery = """
            SELECT 
                COUNT(DISTINCT q.id)
            FROM 
                quizz q
            LEFT JOIN 
                review r ON q.id = r.quizz_id
            LEFT JOIN 
                comment c ON c.quizz_id = q.id
        """,
            nativeQuery = true)
    Page<Object[]> findSuggestedQuizzes(Pageable pageable);
}

