package com.suresoft.sw_test_forum.left_reference.dynamic_test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DynamicTestSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private String searchType2 = "";
    private String searchKeyword2 = "";
}