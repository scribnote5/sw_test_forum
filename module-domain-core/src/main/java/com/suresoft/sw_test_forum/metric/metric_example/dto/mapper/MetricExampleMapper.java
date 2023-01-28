package com.suresoft.sw_test_forum.metric.metric_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExample;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleCommentDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetricExampleMapper extends EntityMapper<MetricExampleDto, MetricExample> {
    MetricExampleMapper INSTANCE = Mappers.getMapper(MetricExampleMapper.class);

    default MetricExampleDto toDtoByCommentList(MetricExampleDto metricExampleDto, List<MetricExampleCommentDto> commentDtoList) {
        for (MetricExampleCommentDto metricExampleCommentDto : commentDtoList) {
            metricExampleDto.getCommentDtoList().add(metricExampleCommentDto);
        }

        return metricExampleDto;
    }
}