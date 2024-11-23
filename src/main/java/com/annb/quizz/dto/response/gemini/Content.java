package com.annb.quizz.dto.response.gemini;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Content {
    private List<Part> parts;
    private String role;
}