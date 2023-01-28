package com.suresoft.sw_test_forum.tool.tool_configuration.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfiguration;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationAttachedFile;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationCommentDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper extends EntityMapper<ToolConfigurationDto, ToolConfiguration> {
    ConfigurationMapper INSTANCE = Mappers.getMapper(ConfigurationMapper.class);

    default ToolConfigurationDto toDtoByAttachedFileList(ToolConfigurationDto toolConfigurationDto, List<ToolConfigurationAttachedFile> attachedFileList) {
        for (ToolConfigurationAttachedFile attachedFile : attachedFileList) {
            toolConfigurationDto.getAttachedFileList().add(attachedFile);
        }

        return toolConfigurationDto;
    }

    default ToolConfigurationDto toDtoByCommentList(ToolConfigurationDto toolConfigurationDto, List<ToolConfigurationCommentDto> commentDtoList) {
        for (ToolConfigurationCommentDto toolConfigurationCommentDto : commentDtoList) {
            toolConfigurationDto.getCommentDtoList().add(toolConfigurationCommentDto);
        }

        return toolConfigurationDto;
    }
}