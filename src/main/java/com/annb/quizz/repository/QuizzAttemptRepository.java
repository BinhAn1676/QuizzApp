package com.annb.quizz.repository;

import com.annb.quizz.entity.QuizzAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface QuizzAttemptRepository extends JpaRepository<QuizzAttempt, String> {
    @Query(value = "SELECT qa.* FROM quizz_attempt qa " +
            "JOIN quizz q ON qa.quizz_id = q.id " +
            "WHERE (:textSearch IS NULL OR qa.created_by LIKE %:textSearch%) " +
            "AND (:status IS NULL OR qa.is_pass = :status) " +
            "AND (:from IS NULL OR DATE(qa.created_at) >= DATE(:from)) " +
            "AND (:to IS NULL OR DATE(qa.created_at) <= DATE(:to))",
            nativeQuery = true)
    Page<QuizzAttempt> findFiltered(@Param("textSearch") String textSearch,
                                    @Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to,
                                    @Param("status") Boolean isPass,
                                    Pageable pageable);
    @Query(value = "SELECT qa FROM QuizzAttempt qa " +
            "JOIN qa.quizz q " +
            "WHERE (:textSearch IS NULL OR qa.createdBy LIKE CONCAT('%', :textSearch, '%')) " +
            "AND (:status IS NULL OR qa.isPass = :status) " +
            "AND (:from IS NULL OR qa.createdAt >= :from) " +
            "AND (:to IS NULL OR qa.createdAt <= :to)")
    Page<QuizzAttempt> findFilteredV2(@Param("textSearch") String textSearch,
                                      @Param("from") LocalDateTime from,
                                      @Param("to") LocalDateTime to,
                                      @Param("status") Boolean isPass,
                                      Pageable pageable);

}
