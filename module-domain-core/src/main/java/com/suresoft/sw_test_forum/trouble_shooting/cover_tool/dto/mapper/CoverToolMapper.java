package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverTool;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoverToolMapper extends EntityMapper<CoverToolDto, CoverTool> {
    CoverToolMapper INSTANCE = Mappers.getMapper(CoverToolMapper.class);

    default CoverToolDto toDtoByAttachedFileList(CoverToolDto coverToolDto, List<CoverToolAttachedFile> attachedFileList) {
        for (CoverToolAttachedFile attachedFile : attachedFileList) {
            coverToolDto.getAttachedFileList().add(attachedFile);
        }

        return coverToolDto;
    }

    default CoverToolDto toDtoByCommentList(CoverToolDto coverToolDto, List<CoverToolCommentDto> commentDtoList) {
        for (CoverToolCommentDto coverToolCommentDto : commentDtoList) {
            coverToolDto.getCommentDtoList().add(coverToolCommentDto);
        }

        return coverToolDto;
    }
}