package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineLike;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsGuidelineLikeMapper extends EntityMapper<JavaCodeConventionsGuidelineLikeDto, JavaCodeConventionsGuidelineLike> {
    JavaCodeConventionsGuidelineLikeMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsGuidelineLikeMapper.class);
}