package com.suresoft.sw_test_forum.metric.metric_guideline.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MetricGuidelineSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
    private long metricIdx = 0;
}