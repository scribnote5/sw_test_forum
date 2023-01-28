package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineLike;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CweJavaGuidelineLikeMapper extends EntityMapper<CweJavaGuidelineLikeDto, CweJavaGuidelineLike> {
    CweJavaGuidelineLikeMapper INSTANCE = Mappers.getMapper(CweJavaGuidelineLikeMapper.class);
}