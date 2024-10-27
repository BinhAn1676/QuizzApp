package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity{
    @Id
    private String id;
    private String content;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "quizz_id")
    private Quizz quizz;
    @Column(name = "question_type")
    private Integer questionType;
}
