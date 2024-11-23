package com.annb.quizz.repository;

import com.annb.quizz.entity.Participant;
import com.annb.quizz.entity.Quizz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {
    @Query(value = "SELECT p.* " +
            "FROM participant p " +
            "JOIN room r ON p.room_id = r.id " +
            "JOIN quizz q ON r.quiz_id = q.id " +
            "WHERE (:roomId IS NULL OR r.id = :roomId) " +
            "AND (:textSearch IS NULL OR r.code LIKE %:textSearch% " +
            "OR p.username LIKE %:textSearch% " +
            "OR p.created_by LIKE %:textSearch%)",
            nativeQuery = true)
    Page<Participant> findFiltered(@Param("textSearch") String textSearch,
                             @Param("roomId") String roomId,
                             Pageable pageable);

}
