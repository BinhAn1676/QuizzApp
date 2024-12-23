package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttemptAnswer {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "quizz_attempt_id")
    private QuizzAttempt quizzAttempt;

    @Column(name = "question_content")
    private String questionContent; // Question content stored as plain text
    @Column(name = "selected_answer_contents")
    private String selectedAnswerContents; // Serialized list of selected Answer contents (as String)
    @Column(name = "correct_answer_contents")
    private String correctAnswerContents; // Serialized list of correct Answer contents (as String)
}