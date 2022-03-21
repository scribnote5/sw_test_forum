package com.suresoft.sw_test_forum.metric.metric_example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MetricExampleSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long metricIdx = 0;
}