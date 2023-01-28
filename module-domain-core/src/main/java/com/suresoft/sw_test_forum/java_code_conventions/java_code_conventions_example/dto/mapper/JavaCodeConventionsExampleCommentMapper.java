package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExampleComment;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsExampleCommentMapper extends EntityMapper<JavaCodeConventionsExampleCommentDto, JavaCodeConventionsExampleComment> {
    JavaCodeConventionsExampleCommentMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsExampleCommentMapper.class);
}