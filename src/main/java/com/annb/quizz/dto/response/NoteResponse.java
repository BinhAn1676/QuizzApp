package com.annb.quizz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    private String id;
    private String createdBy;
    private LocalDateTime createdDate;
    private String note;
    private String quizzId;
    private String questionId;
    private String title;
}
