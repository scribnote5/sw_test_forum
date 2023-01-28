package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExampleComment;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweJavaExampleCommentMapper extends EntityMapper<CweJavaExampleCommentDto, CweJavaExampleComment> {
    CweJavaExampleCommentMapper INSTANCE = Mappers.getMapper(CweJavaExampleCommentMapper.class);
}