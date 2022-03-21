package com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StaticToolSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}