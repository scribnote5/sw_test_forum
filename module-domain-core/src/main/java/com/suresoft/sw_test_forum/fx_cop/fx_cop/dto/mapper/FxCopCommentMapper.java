package com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FxCopCommentMapper extends EntityMapper<FxCopCommentDto, FxCopComment> {
    FxCopCommentMapper INSTANCE = Mappers.getMapper(FxCopCommentMapper.class);
}