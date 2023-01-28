package com.suresoft.sw_test_forum.tool.tool_usage_method.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodComment;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ToolUsageMethodCommentMapper extends EntityMapper<ToolUsageMethodCommentDto, ToolUsageMethodComment> {
    ToolUsageMethodCommentMapper INSTANCE = Mappers.getMapper(ToolUsageMethodCommentMapper.class);
}