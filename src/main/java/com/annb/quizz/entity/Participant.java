package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant extends BaseEntity {
    @Id
    private String id; // Unique participant ID
    private String username; // Username of the participant
    private Integer isActive; // Indicates if the participant is currently active
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room; // The room that the participant is part of
}