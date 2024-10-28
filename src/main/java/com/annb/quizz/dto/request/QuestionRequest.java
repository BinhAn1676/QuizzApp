package com.annb.quizz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    private String content;
    private Integer questionType;
    private String imageUrl;
    private List<AnswerRequest> answers;
}
