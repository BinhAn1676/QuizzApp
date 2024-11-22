package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizz")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Quizz extends BaseEntity{
    @Id
    private String id;
    private String title;
    private String description;
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    // OneToMany relationship to Question
    @OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Note> notes = new ArrayList<>();
}
