package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MisraCppGuidelineSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long misraCppIdx = 0;
}