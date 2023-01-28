package com.suresoft.sw_test_forum.metric.metric_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MetricGuidelineLikeDto extends CommonDto {
    private long metricGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}