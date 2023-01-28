package com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweJavaCommentMapper extends EntityMapper<CweJavaCommentDto, CweJavaComment> {
    CweJavaCommentMapper INSTANCE = Mappers.getMapper(CweJavaCommentMapper.class);
}