package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCppCommentMapper extends EntityMapper<MisraCppCommentDto, MisraCppComment> {
    MisraCppCommentMapper INSTANCE = Mappers.getMapper(MisraCppCommentMapper.class);
}