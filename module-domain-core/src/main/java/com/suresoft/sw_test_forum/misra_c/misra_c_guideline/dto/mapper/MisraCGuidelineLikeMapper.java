package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineLike;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCGuidelineLikeMapper extends EntityMapper<MisraCGuidelineLikeDto, MisraCGuidelineLike> {
    MisraCGuidelineLikeMapper INSTANCE = Mappers.getMapper(MisraCGuidelineLikeMapper.class);
}