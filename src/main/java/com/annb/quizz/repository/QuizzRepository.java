package com.annb.quizz.repository;

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
    @Query("SELECT q FROM Quizz q " +
            "WHERE (:textSearch IS NULL OR q.description LIKE %:textSearch% OR q.title LIKE %:textSearch%) " +
            "AND (:status IS NULL OR q.status = :status)" +
            "AND (:from IS NULL OR q.createdAt >= :from) " +
            "AND (:to IS NULL OR q.createdAt <= :to)")
    Page<Quizz> findFiltered(@Param("textSearch") String textSearch,
                             @Param("from") LocalDateTime from,
                             @Param("to") LocalDateTime to,
                             @Param("status") Integer status,
                             Pageable pageable);
}
