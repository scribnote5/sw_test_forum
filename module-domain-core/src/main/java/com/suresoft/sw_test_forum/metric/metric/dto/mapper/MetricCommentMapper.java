package com.suresoft.sw_test_forum.metric.metric.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MetricCommentMapper extends EntityMapper<MetricCommentDto, MetricComment> {
    MetricCommentMapper INSTANCE = Mappers.getMapper(MetricCommentMapper.class);
}