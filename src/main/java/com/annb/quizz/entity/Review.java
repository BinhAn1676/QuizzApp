package com.annb.quizz.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "quizz_id", nullable = false)
    private Quizz quizz;

    private String username; // ID of the user who left the review
    private String comment; // Review text
    private Double rating; // Rating (e.g., 1 to 5 stars)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
