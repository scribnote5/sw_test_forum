package com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExampleComment;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StyleCopExampleCommentMapper extends EntityMapper<StyleCopExampleCommentDto, StyleCopExampleComment> {
    StyleCopExampleCommentMapper INSTANCE = Mappers.getMapper(StyleCopExampleCommentMapper.class);
}