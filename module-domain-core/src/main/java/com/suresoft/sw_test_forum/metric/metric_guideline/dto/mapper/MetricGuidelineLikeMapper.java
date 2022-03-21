package com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineLike;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MetricGuidelineLikeMapper extends EntityMapper<MetricGuidelineLikeDto, MetricGuidelineLike> {
    MetricGuidelineLikeMapper INSTANCE = Mappers.getMapper(MetricGuidelineLikeMapper.class);
}