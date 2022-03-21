package com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineAttachedFile;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CweGuidelineMapper extends EntityMapper<CweGuidelineDto, CweGuideline> {
    CweGuidelineMapper INSTANCE = Mappers.getMapper(CweGuidelineMapper.class);

    default CweGuidelineDto toDtoByAttachedFileList(CweGuidelineDto cweGuidelineDto, List<CweGuidelineAttachedFile> attachedFileList) {
        for (CweGuidelineAttachedFile attachedFile : attachedFileList) {
            cweGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return cweGuidelineDto;
    }

    default CweGuidelineDto toDtoByCommentList(CweGuidelineDto cweGuidelineDto, List<CweGuidelineCommentDto> commentDtoList) {
        for (CweGuidelineCommentDto cweGuidelineCommentDto : commentDtoList) {
            cweGuidelineDto.getCommentDtoList().add(cweGuidelineCommentDto);
        }

        return cweGuidelineDto;
    }
}