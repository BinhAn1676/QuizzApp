package com.annb.quizz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {
    private String id;
    private String note;
    private String title;
    private String questionId;
    private String quizzId;
    private String username;
    private String textSearch;
}
