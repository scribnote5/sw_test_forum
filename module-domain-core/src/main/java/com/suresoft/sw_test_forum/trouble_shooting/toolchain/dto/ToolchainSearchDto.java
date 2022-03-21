package com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ToolchainSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}