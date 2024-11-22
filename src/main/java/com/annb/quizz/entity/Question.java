package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "point")
    private Double point;
    @Column(name = "time_per_question")
    private Integer time;
    @ManyToOne
    @JoinColumn(name = "quizz_id")
    private Quizz quizz;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();
    @Column(name = "question_type")
    private Integer questionType;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Note> notes = new ArrayList<>();
}
