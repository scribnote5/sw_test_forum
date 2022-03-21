package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolComment;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ControllerTesterToolCommentMapper extends EntityMapper<ControllerTesterToolCommentDto, ControllerTesterToolComment> {
    ControllerTesterToolCommentMapper INSTANCE = Mappers.getMapper(ControllerTesterToolCommentMapper.class);
}