package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineLike;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCppGuidelineLikeMapper extends EntityMapper<MisraCppGuidelineLikeDto, MisraCppGuidelineLike> {
    MisraCppGuidelineLikeMapper INSTANCE = Mappers.getMapper(MisraCppGuidelineLikeMapper.class);
}