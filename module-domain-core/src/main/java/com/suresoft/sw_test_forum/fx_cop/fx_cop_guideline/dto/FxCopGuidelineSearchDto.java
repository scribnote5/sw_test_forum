package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FxCopGuidelineSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long fxCopIdx = 0;
}