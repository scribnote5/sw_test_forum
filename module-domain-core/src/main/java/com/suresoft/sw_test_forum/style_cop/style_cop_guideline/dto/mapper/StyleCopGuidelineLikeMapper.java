package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineLike;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StyleCopGuidelineLikeMapper extends EntityMapper<StyleCopGuidelineLikeDto, StyleCopGuidelineLike> {
    StyleCopGuidelineLikeMapper INSTANCE = Mappers.getMapper(StyleCopGuidelineLikeMapper.class);
}