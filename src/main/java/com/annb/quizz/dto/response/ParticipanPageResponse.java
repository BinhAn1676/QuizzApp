package com.annb.quizz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipanPageResponse {
    private String id;
    private String username;
    private Integer isActive;
    private Double score;
    private LocalDateTime createdAt;
}
