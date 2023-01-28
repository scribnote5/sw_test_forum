package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuideline;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineAttachedFile;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StyleCopGuidelineMapper extends EntityMapper<StyleCopGuidelineDto, StyleCopGuideline> {
    StyleCopGuidelineMapper INSTANCE = Mappers.getMapper(StyleCopGuidelineMapper.class);

    default StyleCopGuidelineDto toDtoByAttachedFileList(StyleCopGuidelineDto styleCopGuidelineDto, List<StyleCopGuidelineAttachedFile> attachedFileList) {
        for (StyleCopGuidelineAttachedFile attachedFile : attachedFileList) {
            styleCopGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return styleCopGuidelineDto;
    }

    default StyleCopGuidelineDto toDtoByCommentList(StyleCopGuidelineDto styleCopGuidelineDto, List<StyleCopGuidelineCommentDto> commentDtoList) {
        for (StyleCopGuidelineCommentDto styleCopGuidelineCommentDto : commentDtoList) {
            styleCopGuidelineDto.getCommentDtoList().add(styleCopGuidelineCommentDto);
        }

        return styleCopGuidelineDto;
    }
}