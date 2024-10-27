package com.annb.quizz.repository;

import com.annb.quizz.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {
    Optional<Topic> findByCode(String code);
}
