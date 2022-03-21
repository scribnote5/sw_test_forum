package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterTool;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ControllerTesterToolMapper extends EntityMapper<ControllerTesterToolDto, ControllerTesterTool> {
    ControllerTesterToolMapper INSTANCE = Mappers.getMapper(ControllerTesterToolMapper.class);

    default ControllerTesterToolDto toDtoByAttachedFileList(ControllerTesterToolDto controllerTesterToolDto, List<ControllerTesterToolAttachedFile> attachedFileList) {
        for (ControllerTesterToolAttachedFile attachedFile : attachedFileList) {
            controllerTesterToolDto.getAttachedFileList().add(attachedFile);
        }

        return controllerTesterToolDto;
    }

    default ControllerTesterToolDto toDtoByCommentList(ControllerTesterToolDto controllerTesterToolDto, List<ControllerTesterToolCommentDto> commentDtoList) {
        for (ControllerTesterToolCommentDto controllerTesterToolCommentDto : commentDtoList) {
            controllerTesterToolDto.getCommentDtoList().add(controllerTesterToolCommentDto);
        }

        return controllerTesterToolDto;
    }
}