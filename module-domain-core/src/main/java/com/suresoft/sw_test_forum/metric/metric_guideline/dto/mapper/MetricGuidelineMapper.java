package com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuideline;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineAttachedFile;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineCommentDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetricGuidelineMapper extends EntityMapper<MetricGuidelineDto, MetricGuideline> {
    MetricGuidelineMapper INSTANCE = Mappers.getMapper(MetricGuidelineMapper.class);

    default MetricGuidelineDto toDtoByAttachedFileList(MetricGuidelineDto metricGuidelineDto, List<MetricGuidelineAttachedFile> attachedFileList) {
        for (MetricGuidelineAttachedFile attachedFile : attachedFileList) {
            metricGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return metricGuidelineDto;
    }

    default MetricGuidelineDto toDtoByCommentList(MetricGuidelineDto metricGuidelineDto, List<MetricGuidelineCommentDto> commentDtoList) {
        for (MetricGuidelineCommentDto metricGuidelineCommentDto : commentDtoList) {
            metricGuidelineDto.getCommentDtoList().add(metricGuidelineCommentDto);
        }

        return metricGuidelineDto;
    }
}