package com.annb.quizz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private String id;
    private String content;
    private Double point;
    private Integer time;
    private Integer questionType;
    private String imageUrl;
    private List<AnswerResponse> answers;
}
