package com.suresoft.sw_test_forum.tool.tool_configuration.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationComment;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConfigurationCommentMapper extends EntityMapper<ToolConfigurationCommentDto, ToolConfigurationComment> {
    ConfigurationCommentMapper INSTANCE = Mappers.getMapper(ConfigurationCommentMapper.class);
}