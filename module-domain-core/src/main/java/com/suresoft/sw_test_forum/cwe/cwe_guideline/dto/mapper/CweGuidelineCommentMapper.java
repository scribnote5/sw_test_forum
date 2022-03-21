package com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineComment;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweGuidelineCommentMapper extends EntityMapper<CweGuidelineCommentDto, CweGuidelineComment> {
    CweGuidelineCommentMapper INSTANCE = Mappers.getMapper(CweGuidelineCommentMapper.class);
}