package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExample;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FxCopExampleMapper extends EntityMapper<FxCopExampleDto, FxCopExample> {
    FxCopExampleMapper INSTANCE = Mappers.getMapper(FxCopExampleMapper.class);

    default FxCopExampleDto toDtoByCommentList(FxCopExampleDto fxCopExampleDto, List<FxCopExampleCommentDto> commentDtoList) {
        for (FxCopExampleCommentDto fxCopExampleCommentDto : commentDtoList) {
            fxCopExampleDto.getCommentDtoList().add(fxCopExampleCommentDto);
        }

        return fxCopExampleDto;
    }
}