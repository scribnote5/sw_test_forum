package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuideline;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineAttachedFile;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsGuidelineMapper extends EntityMapper<JavaCodeConventionsGuidelineDto, JavaCodeConventionsGuideline> {
    JavaCodeConventionsGuidelineMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsGuidelineMapper.class);

    default JavaCodeConventionsGuidelineDto toDtoByAttachedFileList(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto, List<JavaCodeConventionsGuidelineAttachedFile> attachedFileList) {
        for (JavaCodeConventionsGuidelineAttachedFile attachedFile : attachedFileList) {
            javaCodeConventionsGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return javaCodeConventionsGuidelineDto;
    }

    default JavaCodeConventionsGuidelineDto toDtoByCommentList(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto, List<JavaCodeConventionsGuidelineCommentDto> commentDtoList) {
        for (JavaCodeConventionsGuidelineCommentDto javaCodeConventionsGuidelineCommentDto : commentDtoList) {
            javaCodeConventionsGuidelineDto.getCommentDtoList().add(javaCodeConventionsGuidelineCommentDto);
        }

        return javaCodeConventionsGuidelineDto;
    }
}