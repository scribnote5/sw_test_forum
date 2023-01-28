package com.suresoft.sw_test_forum.left_reference.report.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.report.domain.Report;
import com.suresoft.sw_test_forum.left_reference.report.domain.ReportAttachedFile;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportCommentDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDto, Report> {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    default ReportDto toDtoByAttachedFileList(ReportDto reportDto, List<ReportAttachedFile> attachedFileList) {
        for (ReportAttachedFile attachedFile : attachedFileList) {
            reportDto.getAttachedFileList().add(attachedFile);
        }

        return reportDto;
    }

    default ReportDto toDtoByCommentList(ReportDto reportDto, List<ReportCommentDto> commentDtoList) {
        for (ReportCommentDto reportCommentDto : commentDtoList) {
            reportDto.getCommentDtoList().add(reportCommentDto);
        }

        return reportDto;
    }
}