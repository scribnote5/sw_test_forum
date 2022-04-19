package com.suresoft.sw_test_forum.style_cop.style_cop_example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StyleCopExampleSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long styleCopIdx = 0;
}