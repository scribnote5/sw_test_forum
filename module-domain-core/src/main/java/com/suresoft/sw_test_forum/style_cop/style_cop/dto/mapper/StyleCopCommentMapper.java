package com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StyleCopCommentMapper extends EntityMapper<StyleCopCommentDto, StyleCopComment> {
    StyleCopCommentMapper INSTANCE = Mappers.getMapper(StyleCopCommentMapper.class);
}