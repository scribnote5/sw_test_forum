package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MisraCppSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}