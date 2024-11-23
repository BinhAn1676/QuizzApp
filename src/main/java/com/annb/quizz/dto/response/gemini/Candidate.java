package com.annb.quizz.dto.response.gemini;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candidate {
    private Content content;
    private String finishReason;
    private Double avgLogprobs;
}