package com.suresoft.sw_test_forum.metric.metric_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExampleComment;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MetricExampleCommentMapper extends EntityMapper<MetricExampleCommentDto, MetricExampleComment> {
    MetricExampleCommentMapper INSTANCE = Mappers.getMapper(MetricExampleCommentMapper.class);
}