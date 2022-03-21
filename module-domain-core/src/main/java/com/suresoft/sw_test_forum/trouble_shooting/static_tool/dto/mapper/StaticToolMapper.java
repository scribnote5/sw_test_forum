package com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticTool;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaticToolMapper extends EntityMapper<StaticToolDto, StaticTool> {
    StaticToolMapper INSTANCE = Mappers.getMapper(StaticToolMapper.class);

    default StaticToolDto toDtoByAttachedFileList(StaticToolDto staticToolDto, List<StaticToolAttachedFile> attachedFileList) {
        for (StaticToolAttachedFile attachedFile : attachedFileList) {
            staticToolDto.getAttachedFileList().add(attachedFile);
        }

        return staticToolDto;
    }

    default StaticToolDto toDtoByCommentList(StaticToolDto staticToolDto, List<StaticToolCommentDto> commentDtoList) {
        for (StaticToolCommentDto staticToolCommentDto : commentDtoList) {
            staticToolDto.getCommentDtoList().add(staticToolCommentDto);
        }

        return staticToolDto;
    }
}