package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExampleComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCppExampleCommentMapper extends EntityMapper<MisraCppExampleCommentDto, MisraCppExampleComment> {
    MisraCppExampleCommentMapper INSTANCE = Mappers.getMapper(MisraCppExampleCommentMapper.class);
}