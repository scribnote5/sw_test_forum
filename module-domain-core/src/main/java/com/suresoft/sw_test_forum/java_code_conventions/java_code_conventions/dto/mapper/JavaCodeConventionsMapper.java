package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventions;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsAttachedFile;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsMapper extends EntityMapper<JavaCodeConventionsDto, JavaCodeConventions> {
    JavaCodeConventionsMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsMapper.class);

    default JavaCodeConventionsDto toDtoByExample(JavaCodeConventionsDto javaCodeConventionsDto, List<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList) {
        for (JavaCodeConventionsExampleDto javaCodeConventionsExampleDto : javaCodeConventionsExampleDtoList) {
            javaCodeConventionsDto.getJavaCodeConventionsExampleDtoList().add(javaCodeConventionsExampleDto);
        }

        return javaCodeConventionsDto;
    }

    default JavaCodeConventionsDto toDtoByGuideline(JavaCodeConventionsDto javaCodeConventionsDto, List<JavaCodeConventionsGuidelineDto> javaCodeConventionsGuidelineDtoList) {
        for (JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto : javaCodeConventionsGuidelineDtoList) {
            javaCodeConventionsDto.getJavaCodeConventionsGuidelineDtoList().add(javaCodeConventionsGuidelineDto);
        }

        return javaCodeConventionsDto;
    }

    default JavaCodeConventionsDto toDtoByAttachedFileList(JavaCodeConventionsDto javaCodeConventionsDto, List<JavaCodeConventionsAttachedFile> attachedFileList) {
        for (JavaCodeConventionsAttachedFile attachedFile : attachedFileList) {
            javaCodeConventionsDto.getAttachedFileList().add(attachedFile);
        }

        return javaCodeConventionsDto;
    }

    default JavaCodeConventionsDto toDtoByCommentList(JavaCodeConventionsDto javaCodeConventionsDto, List<JavaCodeConventionsCommentDto> commentDtoList) {
        for (JavaCodeConventionsCommentDto javaCodeConventionsCommentDto : commentDtoList) {
            javaCodeConventionsDto.getCommentDtoList().add(javaCodeConventionsCommentDto);
        }

        return javaCodeConventionsDto;
    }
}