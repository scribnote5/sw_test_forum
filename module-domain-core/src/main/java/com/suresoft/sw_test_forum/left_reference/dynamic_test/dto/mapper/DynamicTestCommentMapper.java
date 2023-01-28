package com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestComment;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DynamicTestCommentMapper extends EntityMapper<DynamicTestCommentDto, DynamicTestComment> {
    DynamicTestCommentMapper INSTANCE = Mappers.getMapper(DynamicTestCommentMapper.class);
}