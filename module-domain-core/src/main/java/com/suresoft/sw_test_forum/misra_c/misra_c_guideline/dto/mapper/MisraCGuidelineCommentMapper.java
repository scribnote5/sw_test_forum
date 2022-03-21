package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineComment;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCGuidelineCommentMapper extends EntityMapper<MisraCGuidelineCommentDto, MisraCGuidelineComment> {
    MisraCGuidelineCommentMapper INSTANCE = Mappers.getMapper(MisraCGuidelineCommentMapper.class);
}