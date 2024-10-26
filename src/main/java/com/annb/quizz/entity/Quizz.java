package com.annb.quizz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String status;
}
