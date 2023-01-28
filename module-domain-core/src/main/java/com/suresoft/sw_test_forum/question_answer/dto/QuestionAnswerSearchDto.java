package com.suresoft.sw_test_forum.question_answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionAnswerSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}