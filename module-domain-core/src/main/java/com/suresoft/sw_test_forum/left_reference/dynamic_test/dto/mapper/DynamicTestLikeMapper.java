package com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestLike;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DynamicTestLikeMapper extends EntityMapper<DynamicTestLikeDto, DynamicTestLike> {
    DynamicTestLikeMapper INSTANCE = Mappers.getMapper(DynamicTestLikeMapper.class);
}