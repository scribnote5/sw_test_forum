package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCppGuidelineCommentMapper extends EntityMapper<MisraCppGuidelineCommentDto, MisraCppGuidelineComment> {
    MisraCppGuidelineCommentMapper INSTANCE = Mappers.getMapper(MisraCppGuidelineCommentMapper.class);
}