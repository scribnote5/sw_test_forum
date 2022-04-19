package com.suresoft.sw_test_forum.fx_cop.fx_cop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FxCopSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}