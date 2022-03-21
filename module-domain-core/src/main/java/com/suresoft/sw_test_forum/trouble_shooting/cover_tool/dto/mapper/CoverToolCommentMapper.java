package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolComment;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CoverToolCommentMapper extends EntityMapper<CoverToolCommentDto, CoverToolComment> {
    CoverToolCommentMapper INSTANCE = Mappers.getMapper(CoverToolCommentMapper.class);
}