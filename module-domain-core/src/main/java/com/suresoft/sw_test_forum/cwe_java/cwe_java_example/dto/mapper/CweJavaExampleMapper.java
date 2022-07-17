package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExample;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CweJavaExampleMapper extends EntityMapper<CweJavaExampleDto, CweJavaExample> {
    CweJavaExampleMapper INSTANCE = Mappers.getMapper(CweJavaExampleMapper.class);

    default CweJavaExampleDto toDtoByCommentList(CweJavaExampleDto cweJavaExampleDto, List<CweJavaExampleCommentDto> commentDtoList) {
        for (CweJavaExampleCommentDto cweJavaExampleCommentDto : commentDtoList) {
            cweJavaExampleDto.getCommentDtoList().add(cweJavaExampleCommentDto);
        }

        return cweJavaExampleDto;
    }
}