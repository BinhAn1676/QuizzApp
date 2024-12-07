package com.annb.quizz.dto.response;

import com.annb.quizz.entity.Participant;
import com.annb.quizz.entity.Quizz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private String id; // Unique room ID
    private String code; // Code for users to join the room
    private Boolean isActive; // Indicates if the room is active
    private String quizzId;
    private String createdBy;

}
