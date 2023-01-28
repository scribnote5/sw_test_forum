package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineAttachedFile;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CweJavaGuidelineMapper extends EntityMapper<CweJavaGuidelineDto, CweJavaGuideline> {
    CweJavaGuidelineMapper INSTANCE = Mappers.getMapper(CweJavaGuidelineMapper.class);

    default CweJavaGuidelineDto toDtoByAttachedFileList(CweJavaGuidelineDto cweJavaGuidelineDto, List<CweJavaGuidelineAttachedFile> attachedFileList) {
        for (CweJavaGuidelineAttachedFile attachedFile : attachedFileList) {
            cweJavaGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return cweJavaGuidelineDto;
    }

    default CweJavaGuidelineDto toDtoByCommentList(CweJavaGuidelineDto cweJavaGuidelineDto, List<CweJavaGuidelineCommentDto> commentDtoList) {
        for (CweJavaGuidelineCommentDto cweJavaGuidelineCommentDto : commentDtoList) {
            cweJavaGuidelineDto.getCommentDtoList().add(cweJavaGuidelineCommentDto);
        }

        return cweJavaGuidelineDto;
    }
}