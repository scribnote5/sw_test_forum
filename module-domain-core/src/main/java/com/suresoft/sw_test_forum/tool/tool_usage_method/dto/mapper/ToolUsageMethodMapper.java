package com.suresoft.sw_test_forum.tool.tool_usage_method.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethod;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodAttachedFile;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodCommentDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToolUsageMethodMapper extends EntityMapper<ToolUsageMethodDto, ToolUsageMethod> {
    ToolUsageMethodMapper INSTANCE = Mappers.getMapper(ToolUsageMethodMapper.class);

    default ToolUsageMethodDto toDtoByAttachedFileList(ToolUsageMethodDto toolUsageMethodDto, List<ToolUsageMethodAttachedFile> attachedFileList) {
        for (ToolUsageMethodAttachedFile attachedFile : attachedFileList) {
            toolUsageMethodDto.getAttachedFileList().add(attachedFile);
        }

        return toolUsageMethodDto;
    }

    default ToolUsageMethodDto toDtoByCommentList(ToolUsageMethodDto toolUsageMethodDto, List<ToolUsageMethodCommentDto> commentDtoList) {
        for (ToolUsageMethodCommentDto toolUsageMethodCommentDto : commentDtoList) {
            toolUsageMethodDto.getCommentDtoList().add(toolUsageMethodCommentDto);
        }

        return toolUsageMethodDto;
    }
}