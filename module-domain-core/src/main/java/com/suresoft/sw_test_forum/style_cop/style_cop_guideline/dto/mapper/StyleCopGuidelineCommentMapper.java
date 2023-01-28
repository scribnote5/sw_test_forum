package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineComment;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StyleCopGuidelineCommentMapper extends EntityMapper<StyleCopGuidelineCommentDto, StyleCopGuidelineComment> {
    StyleCopGuidelineCommentMapper INSTANCE = Mappers.getMapper(StyleCopGuidelineCommentMapper.class);
}