package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExampleComment;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FxCopExampleCommentMapper extends EntityMapper<FxCopExampleCommentDto, FxCopExampleComment> {
    FxCopExampleCommentMapper INSTANCE = Mappers.getMapper(FxCopExampleCommentMapper.class);
}