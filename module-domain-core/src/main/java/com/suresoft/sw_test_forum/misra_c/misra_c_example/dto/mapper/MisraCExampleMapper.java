package com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExample;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MisraCExampleMapper extends EntityMapper<MisraCExampleDto, MisraCExample> {
    MisraCExampleMapper INSTANCE = Mappers.getMapper(MisraCExampleMapper.class);

    default MisraCExampleDto toDtoByCommentList(MisraCExampleDto misraCExampleDto, List<MisraCExampleCommentDto> commentDtoList) {
        for (MisraCExampleCommentDto misraCExampleCommentDto : commentDtoList) {
            misraCExampleDto.getCommentDtoList().add(misraCExampleCommentDto);
        }

        return misraCExampleDto;
    }
}