package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExample;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JavaCodeConventionsExampleMapper extends EntityMapper<JavaCodeConventionsExampleDto, JavaCodeConventionsExample> {
    JavaCodeConventionsExampleMapper INSTANCE = Mappers.getMapper(JavaCodeConventionsExampleMapper.class);

    default JavaCodeConventionsExampleDto toDtoByCommentList(JavaCodeConventionsExampleDto javaCodeConventionsExampleDto, List<JavaCodeConventionsExampleCommentDto> commentDtoList) {
        for (JavaCodeConventionsExampleCommentDto javaCodeConventionsExampleCommentDto : commentDtoList) {
            javaCodeConventionsExampleDto.getCommentDtoList().add(javaCodeConventionsExampleCommentDto);
        }

        return javaCodeConventionsExampleDto;
    }
}