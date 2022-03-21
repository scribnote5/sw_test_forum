package com.suresoft.sw_test_forum.metric.metric.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MetricSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}