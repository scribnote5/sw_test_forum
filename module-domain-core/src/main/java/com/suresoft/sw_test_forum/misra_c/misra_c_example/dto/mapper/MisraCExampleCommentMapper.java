package com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExampleComment;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCExampleCommentMapper extends EntityMapper<MisraCExampleCommentDto, MisraCExampleComment> {
    MisraCExampleCommentMapper INSTANCE = Mappers.getMapper(MisraCExampleCommentMapper.class);
}