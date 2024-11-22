package com.annb.quizz.repository;

import com.annb.quizz.entity.Note;
import com.annb.quizz.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String>, JpaSpecificationExecutor<Note> {
}
