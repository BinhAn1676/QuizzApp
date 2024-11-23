package com.annb.quizz.dto.response.gemini;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeminiResponse {
    private List<Candidate> candidates;
    private String modelVersion;
}