package com.annb.quizz.dto;

import lombok.Data;

import java.util.List;
@Data
public class AttemptDTO {
    private String questionContent;
    private List<String> selectedAnswerContents;
    private List<String> correctAnswerContents;
}
