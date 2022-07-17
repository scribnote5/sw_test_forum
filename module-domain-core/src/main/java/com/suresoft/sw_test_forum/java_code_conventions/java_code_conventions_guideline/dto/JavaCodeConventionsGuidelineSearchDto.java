package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JavaCodeConventionsGuidelineSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long javaCodeConventionsIdx = 0;
}