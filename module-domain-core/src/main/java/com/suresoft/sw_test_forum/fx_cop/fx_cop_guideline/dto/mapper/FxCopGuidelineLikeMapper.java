package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineLike;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FxCopGuidelineLikeMapper extends EntityMapper<FxCopGuidelineLikeDto, FxCopGuidelineLike> {
    FxCopGuidelineLikeMapper INSTANCE = Mappers.getMapper(FxCopGuidelineLikeMapper.class);
}