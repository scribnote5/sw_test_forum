package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineComment;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweJavaGuidelineCommentMapper extends EntityMapper<CweJavaGuidelineCommentDto, CweJavaGuidelineComment> {
    CweJavaGuidelineCommentMapper INSTANCE = Mappers.getMapper(CweJavaGuidelineCommentMapper.class);
}