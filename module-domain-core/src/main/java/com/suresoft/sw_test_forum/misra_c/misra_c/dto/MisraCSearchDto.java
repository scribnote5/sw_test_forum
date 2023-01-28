package com.suresoft.sw_test_forum.misra_c.misra_c.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MisraCSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}