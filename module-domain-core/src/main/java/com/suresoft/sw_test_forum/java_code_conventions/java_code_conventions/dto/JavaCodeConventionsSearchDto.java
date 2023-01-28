package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JavaCodeConventionsSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}