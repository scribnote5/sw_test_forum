package com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTest;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestAttachedFile;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestCommentDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DynamicTestMapper extends EntityMapper<DynamicTestDto, DynamicTest> {
    DynamicTestMapper INSTANCE = Mappers.getMapper(DynamicTestMapper.class);

    default DynamicTestDto toDtoByAttachedFileList(DynamicTestDto dynamicTest, List<DynamicTestAttachedFile> attachedFileList) {
        for (DynamicTestAttachedFile attachedFile : attachedFileList) {
            dynamicTest.getAttachedFileList().add(attachedFile);
        }

        return dynamicTest;
    }

    default DynamicTestDto toDtoByCommentList(DynamicTestDto dynamicTest, List<DynamicTestCommentDto> commentDtoList) {
        for (DynamicTestCommentDto dynamicTestCommentDto : commentDtoList) {
            dynamicTest.getCommentDtoList().add(dynamicTestCommentDto);
        }

        return dynamicTest;
    }
}