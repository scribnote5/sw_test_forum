package com.suresoft.sw_test_forum.cwe.cwe_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExampleComment;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweExampleCommentMapper extends EntityMapper<CweExampleCommentDto, CweExampleComment> {
    CweExampleCommentMapper INSTANCE = Mappers.getMapper(CweExampleCommentMapper.class);
}