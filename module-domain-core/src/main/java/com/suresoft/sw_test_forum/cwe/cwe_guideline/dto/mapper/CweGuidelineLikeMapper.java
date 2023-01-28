package com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineLike;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweGuidelineLikeMapper extends EntityMapper<CweGuidelineLikeDto, CweGuidelineLike> {
    CweGuidelineLikeMapper INSTANCE = Mappers.getMapper(CweGuidelineLikeMapper.class);
}