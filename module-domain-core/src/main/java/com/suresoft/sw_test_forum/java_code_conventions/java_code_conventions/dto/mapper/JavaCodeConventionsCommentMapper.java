package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsCommentMapper extends EntityMapper<JavaCodeConventionsCommentDto, JavaCodeConventionsComment> {
    JavaCodeConventionsCommentMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsCommentMapper.class);
}