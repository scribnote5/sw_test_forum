package com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolComment;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StaticToolCommentMapper extends EntityMapper<StaticToolCommentDto, StaticToolComment> {
    StaticToolCommentMapper INSTANCE = Mappers.getMapper(StaticToolCommentMapper.class);
}