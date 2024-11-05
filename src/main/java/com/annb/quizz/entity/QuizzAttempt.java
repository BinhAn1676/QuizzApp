package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quizz_attempt")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizzAttempt extends BaseEntity{
    @Id
    private String id;
    private Double score;
    @ManyToOne
    @JoinColumn(name = "quizz_id")
    private Quizz quizz;
    @Column(name = "is_pass")
    private Boolean isPass;
}