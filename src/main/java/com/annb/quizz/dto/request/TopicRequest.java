package com.annb.quizz.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicRequest {
    @NotEmpty(message = "code cant be empty")
    @NotNull(message = "code cant be null")
    private String code;
    @NotEmpty(message = "title cant be empty")
    @NotNull(message = "title cant be null")
    private String title;
    private String description;

}
