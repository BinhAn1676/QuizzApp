package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Answer extends BaseEntity{
    @Id
    private String id;
    private String content;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
