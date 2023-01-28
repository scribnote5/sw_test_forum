package com.suresoft.sw_test_forum.cwe.cwe.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweCommentMapper extends EntityMapper<CweCommentDto, CweComment> {
    CweCommentMapper INSTANCE = Mappers.getMapper(CweCommentMapper.class);
}