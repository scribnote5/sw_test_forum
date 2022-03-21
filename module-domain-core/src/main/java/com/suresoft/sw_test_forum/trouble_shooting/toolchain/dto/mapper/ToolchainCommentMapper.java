package com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainComment;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ToolchainCommentMapper extends EntityMapper<ToolchainCommentDto, ToolchainComment> {
    ToolchainCommentMapper INSTANCE = Mappers.getMapper(ToolchainCommentMapper.class);
}