package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "note")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Note extends BaseEntity{
    @Id
    private String id;
    private String note;
    private String title;
    @ManyToOne
    @JoinColumn(name = "quizz_id")
    private Quizz quizz;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
