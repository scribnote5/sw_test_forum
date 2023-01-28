package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShooting;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingAttachedFile;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingCommentDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToolTroubleShootingMapper extends EntityMapper<ToolTroubleShootingDto, ToolTroubleShooting> {
    ToolTroubleShootingMapper INSTANCE = Mappers.getMapper(ToolTroubleShootingMapper.class);

    default ToolTroubleShootingDto toDtoByAttachedFileList(ToolTroubleShootingDto toolTroubleShootingDto, List<ToolTroubleShootingAttachedFile> attachedFileList) {
        for (ToolTroubleShootingAttachedFile attachedFile : attachedFileList) {
            toolTroubleShootingDto.getAttachedFileList().add(attachedFile);
        }

        return toolTroubleShootingDto;
    }

    default ToolTroubleShootingDto toDtoByCommentList(ToolTroubleShootingDto toolTroubleShootingDto, List<ToolTroubleShootingCommentDto> commentDtoList) {
        for (ToolTroubleShootingCommentDto toolTroubleShootingCommentDto : commentDtoList) {
            toolTroubleShootingDto.getCommentDtoList().add(toolTroubleShootingCommentDto);
        }

        return toolTroubleShootingDto;
    }
}