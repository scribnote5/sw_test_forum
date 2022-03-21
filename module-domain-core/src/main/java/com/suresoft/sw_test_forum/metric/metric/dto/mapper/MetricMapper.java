package com.suresoft.sw_test_forum.metric.metric.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric.domain.Metric;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricAttachedFile;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricCommentDto;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetricMapper extends EntityMapper<MetricDto, Metric> {
    MetricMapper INSTANCE = Mappers.getMapper(MetricMapper.class);

    default MetricDto toDtoByExample(MetricDto metricDto, List<MetricExampleDto> metricExampleDtoList) {
        for (MetricExampleDto metricExampleDto : metricExampleDtoList) {
            metricDto.getMetricExampleDtoList().add(metricExampleDto);
        }

        return metricDto;
    }

    default MetricDto toDtoByGuideline(MetricDto metricDto, List<MetricGuidelineDto> metricGuidelineDtoList) {
        for (MetricGuidelineDto metricGuidelineDto : metricGuidelineDtoList) {
            metricDto.getMetricGuidelineDtoList().add(metricGuidelineDto);
        }

        return metricDto;
    }

    default MetricDto toDtoByAttachedFileList(MetricDto metricDto, List<MetricAttachedFile> attachedFileList) {
        for (MetricAttachedFile attachedFile : attachedFileList) {
            metricDto.getAttachedFileList().add(attachedFile);
        }

        return metricDto;
    }

    default MetricDto toDtoByCommentList(MetricDto metricDto, List<MetricCommentDto> commentDtoList) {
        for (MetricCommentDto metricCommentDto : commentDtoList) {
            metricDto.getCommentDtoList().add(metricCommentDto);
        }

        return metricDto;
    }
}