package com.annb.quizz.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CollectionId;

@Entity
@Table(name = "topic")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Topic extends BaseEntity{
    @Id
    private String id;
    private String title;
    private String description;
    @Column(unique = true)
    private String code;

}
