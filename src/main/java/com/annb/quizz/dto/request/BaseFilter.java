package com.annb.quizz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseFilter {
    private Integer pageSize = 5;
    private Integer pageNo = 0;
    private LocalDateTime from;
    private LocalDateTime to;
    private String textSearch;
    private Integer status;
}
