package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineComment;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsGuidelineCommentMapper extends EntityMapper<JavaCodeConventionsGuidelineCommentDto, JavaCodeConventionsGuidelineComment> {
    JavaCodeConventionsGuidelineCommentMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsGuidelineCommentMapper.class);
}