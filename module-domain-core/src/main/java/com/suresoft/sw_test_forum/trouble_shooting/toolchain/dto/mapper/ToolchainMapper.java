package com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.Toolchain;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToolchainMapper extends EntityMapper<ToolchainDto, Toolchain> {
    ToolchainMapper INSTANCE = Mappers.getMapper(ToolchainMapper.class);

    default ToolchainDto toDtoByAttachedFileList(ToolchainDto toolchainDto, List<ToolchainAttachedFile> attachedFileList) {
        for (ToolchainAttachedFile attachedFile : attachedFileList) {
            toolchainDto.getAttachedFileList().add(attachedFile);
        }

        return toolchainDto;
    }

    default ToolchainDto toDtoByCommentList(ToolchainDto toolchainDto, List<ToolchainCommentDto> commentDtoList) {
        for (ToolchainCommentDto toolchainCommentDto : commentDtoList) {
            toolchainDto.getCommentDtoList().add(toolchainCommentDto);
        }

        return toolchainDto;
    }
}