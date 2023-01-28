package com.suresoft.sw_test_forum.cwe.cwe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CweSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}