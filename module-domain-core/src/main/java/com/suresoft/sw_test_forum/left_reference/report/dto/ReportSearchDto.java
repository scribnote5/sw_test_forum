package com.suresoft.sw_test_forum.left_reference.report.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReportSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private String searchType2 = "";
    private String searchKeyword2 = "";
}