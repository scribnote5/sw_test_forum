package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineComment;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FxCopGuidelineCommentMapper extends EntityMapper<FxCopGuidelineCommentDto, FxCopGuidelineComment> {
    FxCopGuidelineCommentMapper INSTANCE = Mappers.getMapper(FxCopGuidelineCommentMapper.class);
}