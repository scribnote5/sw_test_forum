package com.suresoft.sw_test_forum.cwe_java.cwe_java.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CweJavaSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}