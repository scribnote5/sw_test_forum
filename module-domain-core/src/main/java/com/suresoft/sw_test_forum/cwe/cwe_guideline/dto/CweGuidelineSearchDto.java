package com.suresoft.sw_test_forum.cwe.cwe_guideline.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CweGuidelineSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long cweIdx = 0;
}