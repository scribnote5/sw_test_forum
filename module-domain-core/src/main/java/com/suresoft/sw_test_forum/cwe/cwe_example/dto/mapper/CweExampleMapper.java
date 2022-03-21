package com.suresoft.sw_test_forum.cwe.cwe_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExample;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CweExampleMapper extends EntityMapper<CweExampleDto, CweExample> {
    CweExampleMapper INSTANCE = Mappers.getMapper(CweExampleMapper.class);

    default CweExampleDto toDtoByCommentList(CweExampleDto cweExampleDto, List<CweExampleCommentDto> commentDtoList) {
        for (CweExampleCommentDto cweExampleCommentDto : commentDtoList) {
            cweExampleDto.getCommentDtoList().add(cweExampleCommentDto);
        }

        return cweExampleDto;
    }
}