package com.annb.quizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseEntity {
    @Id
    private String id; // Unique room ID
    private String code; // Code for users to join the room
    private Boolean isActive; // Indicates if the room is active
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quizz quiz; // The quiz associated with the room
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
}
