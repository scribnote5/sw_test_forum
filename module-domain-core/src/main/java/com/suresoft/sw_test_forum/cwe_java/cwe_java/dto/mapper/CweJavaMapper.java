package com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJava;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaAttachedFile;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CweJavaMapper extends EntityMapper<CweJavaDto, CweJava> {
    CweJavaMapper INSTANCE = Mappers.getMapper(CweJavaMapper.class);

    default CweJavaDto toDtoByExample(CweJavaDto cweJavaDto, List<CweJavaExampleDto> cweJavaExampleDtoList) {
        for (CweJavaExampleDto cweJavaExampleDto : cweJavaExampleDtoList) {
            cweJavaDto.getCweJavaExampleDtoList().add(cweJavaExampleDto);
        }

        return cweJavaDto;
    }

    default CweJavaDto toDtoByGuideline(CweJavaDto cweJavaDto, List<CweJavaGuidelineDto> cweJavaGuidelineDtoList) {
        for (CweJavaGuidelineDto cweJavaGuidelineDto : cweJavaGuidelineDtoList) {
            cweJavaDto.getCweJavaGuidelineDtoList().add(cweJavaGuidelineDto);
        }

        return cweJavaDto;
    }

    default CweJavaDto toDtoByAttachedFileList(CweJavaDto cweJavaDto, List<CweJavaAttachedFile> attachedFileList) {
        for (CweJavaAttachedFile attachedFile : attachedFileList) {
            cweJavaDto.getAttachedFileList().add(attachedFile);
        }

        return cweJavaDto;
    }

    default CweJavaDto toDtoByCommentList(CweJavaDto cweJavaDto, List<CweJavaCommentDto> commentDtoList) {
        for (CweJavaCommentDto cweJavaCommentDto : commentDtoList) {
            cweJavaDto.getCommentDtoList().add(cweJavaCommentDto);
        }

        return cweJavaDto;
    }
}