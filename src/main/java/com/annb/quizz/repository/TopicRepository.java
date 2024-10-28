package com.annb.quizz.repository;

import com.annb.quizz.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {
    Optional<Topic> findByCode(String code);

    @Query("SELECT t FROM Topic t " +
            "WHERE (:textSearch IS NULL OR t.code LIKE %:textSearch% OR t.title LIKE %:textSearch% OR t.description LIKE %:textSearch%) " +
            "AND (:from IS NULL OR t.createdAt >= :from) " +
            "AND (:to IS NULL OR t.createdAt <= :to)")
    Page<Topic> findFiltered(@Param("textSearch") String textSearch,
                             @Param("from") LocalDateTime from,
                             @Param("to") LocalDateTime to,
                             Pageable pageable);
}
