package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingComment;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ToolTroubleShootingCommentMapper extends EntityMapper<ToolTroubleShootingCommentDto, ToolTroubleShootingComment> {
    ToolTroubleShootingCommentMapper INSTANCE = Mappers.getMapper(ToolTroubleShootingCommentMapper.class);
}