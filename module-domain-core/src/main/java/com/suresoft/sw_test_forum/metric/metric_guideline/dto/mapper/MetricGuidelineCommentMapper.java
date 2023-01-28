package com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineComment;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MetricGuidelineCommentMapper extends EntityMapper<MetricGuidelineCommentDto, MetricGuidelineComment> {
    MetricGuidelineCommentMapper INSTANCE = Mappers.getMapper(MetricGuidelineCommentMapper.class);
}