package com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExample;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StyleCopExampleMapper extends EntityMapper<StyleCopExampleDto, StyleCopExample> {
    StyleCopExampleMapper INSTANCE = Mappers.getMapper(StyleCopExampleMapper.class);

    default StyleCopExampleDto toDtoByCommentList(StyleCopExampleDto styleCopExampleDto, List<StyleCopExampleCommentDto> commentDtoList) {
        for (StyleCopExampleCommentDto styleCopExampleCommentDto : commentDtoList) {
            styleCopExampleDto.getCommentDtoList().add(styleCopExampleCommentDto);
        }

        return styleCopExampleDto;
    }
}