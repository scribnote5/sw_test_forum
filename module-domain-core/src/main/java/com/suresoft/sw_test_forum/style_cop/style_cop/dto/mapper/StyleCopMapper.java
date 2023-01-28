package com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCop;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopAttachedFile;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StyleCopMapper extends EntityMapper<StyleCopDto, StyleCop> {
    StyleCopMapper INSTANCE = Mappers.getMapper(StyleCopMapper.class);

    default StyleCopDto toDtoByExample(StyleCopDto styleCopDto, List<StyleCopExampleDto> styleCopExampleDtoList) {
        for (StyleCopExampleDto styleCopExampleDto : styleCopExampleDtoList) {
            styleCopDto.getStyleCopExampleDtoList().add(styleCopExampleDto);
        }

        return styleCopDto;
    }

    default StyleCopDto toDtoByGuideline(StyleCopDto styleCopDto, List<StyleCopGuidelineDto> styleCopGuidelineDtoList) {
        for (StyleCopGuidelineDto styleCopGuidelineDto : styleCopGuidelineDtoList) {
            styleCopDto.getStyleCopGuidelineDtoList().add(styleCopGuidelineDto);
        }

        return styleCopDto;
    }

    default StyleCopDto toDtoByAttachedFileList(StyleCopDto styleCopDto, List<StyleCopAttachedFile> attachedFileList) {
        for (StyleCopAttachedFile attachedFile : attachedFileList) {
            styleCopDto.getAttachedFileList().add(attachedFile);
        }

        return styleCopDto;
    }

    default StyleCopDto toDtoByCommentList(StyleCopDto styleCopDto, List<StyleCopCommentDto> commentDtoList) {
        for (StyleCopCommentDto styleCopCommentDto : commentDtoList) {
            styleCopDto.getCommentDtoList().add(styleCopCommentDto);
        }

        return styleCopDto;
    }
}