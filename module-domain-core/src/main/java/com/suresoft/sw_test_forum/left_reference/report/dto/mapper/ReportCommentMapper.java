package com.suresoft.sw_test_forum.left_reference.report.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.report.domain.ReportComment;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReportCommentMapper extends EntityMapper<ReportCommentDto, ReportComment> {
    ReportCommentMapper INSTANCE = Mappers.getMapper(ReportCommentMapper.class);
}