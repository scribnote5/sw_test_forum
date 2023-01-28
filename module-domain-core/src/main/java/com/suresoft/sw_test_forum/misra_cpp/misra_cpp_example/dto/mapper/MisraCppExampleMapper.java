package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExample;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MisraCppExampleMapper extends EntityMapper<MisraCppExampleDto, MisraCppExample> {
    MisraCppExampleMapper INSTANCE = Mappers.getMapper(MisraCppExampleMapper.class);

    default MisraCppExampleDto toDtoByCommentList(MisraCppExampleDto misraCppExampleDto, List<MisraCppExampleCommentDto> commentDtoList) {
        for (MisraCppExampleCommentDto misraCppExampleCommentDto : commentDtoList) {
            misraCppExampleDto.getCommentDtoList().add(misraCppExampleCommentDto);
        }

        return misraCppExampleDto;
    }
}